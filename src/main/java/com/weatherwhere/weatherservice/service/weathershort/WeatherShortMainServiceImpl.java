package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortEntityListDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortMainRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortSubRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherXYRepository;
import com.weatherwhere.weatherservice.service.date.DateService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherShortMainServiceImpl implements WeatherShortMainService {
    private final WeatherShortMainRepository weatherShortMainRepository;

    private final WeatherShortSubRepository weatherShortSubRepository;

    private final WeatherXYRepository weatherXYRepository;

    private final DateService dateService;


    @Cacheable("xylist")
    @Override
    @Transactional
    /**
     * db에 있는 모든 지역의 격자 x,y 불러와서 list에 저장한 뒤 캐싱
     *
     * @return xyList 리턴
     */
    public List<WeatherXY> splitXyList() throws Exception {
        List<WeatherXY> xyList = weatherXYRepository.findAll();
        System.out.println("xyList================================"+xyList);
        List<WeatherXY> subList = xyList.subList(0, 1);
        return xyList;
    }


    /**
     * 공공데이터 api에 요청할 url 만들어서 리턴하는 메서드
     *
     * @param weatherShortRequestDTO
     * @return url 형식으로 변환한 값 endUrl로 리턴, 예외발생시 호출한 메서드로 Exception 던짐
     * @throws Exception
     */
    private URI makeUri(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = System.getProperty("WEATHER_SHORT_SERVICE_KEY");

        String dataType = "JSON";
        String numOfRows = "302";
        String pageNo = "1";

        dateService.getBaseDateTime(weatherShortRequestDTO);
        String url;
        if(weatherShortRequestDTO.getWeatherXY() != null) {
            url = String.format("%s?serviceKey=%s&pageNo=%s&numOfRows=%s&dataType=%s&base_date=%s&base_time=%s&nx=%s&ny=%s",
                    apiUrl, serviceKey, pageNo, numOfRows, dataType,
                    weatherShortRequestDTO.getBaseDate(), weatherShortRequestDTO.getBaseTime(),
                    weatherShortRequestDTO.getWeatherXY().getWeatherX(), weatherShortRequestDTO.getWeatherXY().getWeatherY());

        }else{
            url = String.format("%s?serviceKey=%s&pageNo=%s&numOfRows=%s&dataType=%s&base_date=%s&base_time=%s&nx=%s&ny=%s",
                    apiUrl, serviceKey, pageNo, numOfRows, dataType,
                    weatherShortRequestDTO.getBaseDate(), weatherShortRequestDTO.getBaseTime(),
                    weatherShortRequestDTO.getNx(), weatherShortRequestDTO.getNy());
        }
        //rest template이 String 문자열을 한 번 더 인코딩 해주는 걸 방지하기 위해 url 객체로 넣음
        URI endUrl = new URI(url);
        log.info("endUrl: " + endUrl);
        return endUrl;

    }


    /**
     * 공공데이터 api로부터 json값 받아와서 파싱한 뒤 날씨 data만 리턴
     *
     * @param weatherShortRequestDTO
     * @return 받은 json data의 resultCode가 00일때는 itemNode(날씨데이터)리턴, 아닐시 예외 처리
     * @throws Exception
     */
    private JsonNode weatherShortJsonParsing(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {

        //http 통신방식 = rest template
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(makeUri(weatherShortRequestDTO), String.class);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonNode 사용해서 response의 값들 tree구조로 읽음
        JsonNode rootNode = objectMapper.readTree(response);
        //item 값만 가져와서 itemNode에 저장
        JsonNode itemNode = rootNode.path("response").path("body").path("items").path("item");
        Integer resultCode = rootNode.path("response").path("header").get("resultCode").asInt();
        if (resultCode == 00) {
            return itemNode;
        } else {
            System.out.println("resultcode" + resultCode);
            throw new Exception("잘못된 요청입니다");
        }


    }

    @Override
    /**
     * 파싱한 json(날씨 데이터)값을 fcstDate+fcstTime을 key로 한 weatherShortAllDTOList에 담아 리턴
     *
     * @param weatherShortRequestDTO
     * @return fcstDate+fcstTime을 key로 한 weatherShortAllDTOList에 날씨 데이터를 담아 리턴, 예외 발생시 Exception 던짐
     * @throws Exception
     */ public List<WeatherShortAllDTO> jsonToFcstDateMap(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {

        JsonNode itemNode = weatherShortJsonParsing(weatherShortRequestDTO);

        List<WeatherShortAllDTO> weatherShortAllDTOList = StreamSupport.stream(itemNode.spliterator(), false)
                //예보날짜+시간을 key값으로 함
                .collect(Collectors.groupingBy(time -> time.get("fcstTime").asText() + time.get("fcstDate").asText()))
                .values().stream()
                //예보날짜+시간순으로 정렬
/*                .sorted(Comparator.comparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstDate").asText())
                        .thenComparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstTime").asText()))*/
                .map(timeList -> JsonToDTOWeatherShort(weatherShortRequestDTO, timeList))
                .collect(Collectors.toList());
                log.info("weatherShortAllDTOList ========"+weatherShortAllDTOList);
        return weatherShortAllDTOList;
    }


    @Override
    /**
     * weatherShortAllDTOList의 시간 당 하나의 dto 요소들 저장한 뒤 WeatherShortAllDTO dto 리턴
     *
     * @param weatherShortRequestDTO
     * @param timeList (12시간)
     * @return WeatherShortAllDTO dto에 baseDate, baseTime, nx, ny, fcstDateTime, pop, pcp ··· 등등 한시간에 들어갈 날씨 데이터들을 담아 리턴
     */
    public WeatherShortAllDTO JsonToDTOWeatherShort(WeatherShortRequestDTO weatherShortRequestDTO, List<JsonNode> timeList) {

        JsonNode time = timeList.get(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        WeatherShortAllDTO dto;

        if(weatherShortRequestDTO.getWeatherXY() != null){
            dto = WeatherShortAllDTO.builder()
                    .baseDate(weatherShortRequestDTO.getBaseDate())
                    .baseTime(weatherShortRequestDTO.getBaseTime())
                    .nx(weatherShortRequestDTO.getWeatherXY().getWeatherX())
                    .ny(weatherShortRequestDTO.getWeatherXY().getWeatherY())
                    .fcstDateTime(LocalDateTime.parse(time.get("fcstDate").asText() + time.get("fcstTime").asText(), dateFormatter))
                    .build();
        }else{
            dto = WeatherShortAllDTO.builder()
                    .baseDate(weatherShortRequestDTO.getBaseDate())
                    .baseTime(weatherShortRequestDTO.getBaseTime())
                    .nx(weatherShortRequestDTO.getNx())
                    .ny(weatherShortRequestDTO.getNy())
                    .fcstDateTime(LocalDateTime.parse(time.get("fcstDate").asText() + time.get("fcstTime").asText(), dateFormatter))
                    .build();
        }


        Map<String, Consumer<JsonNode>> handlers = new HashMap<>();
        handlers.put("POP", node -> dto.setPop(node.get("fcstValue").asDouble()));
        handlers.put("PCP", node -> dto.setPcp(node.get("fcstValue").asText()));
        handlers.put("PTY", node -> dto.setPty(node.get("fcstValue").asDouble()));
        handlers.put("SKY", node -> dto.setSky(node.get("fcstValue").asDouble()));
        handlers.put("WSD", node -> dto.setWsd(node.get("fcstValue").asDouble()));
        handlers.put("REH", node -> dto.setReh(node.get("fcstValue").asDouble()));
        handlers.put("TMP", node -> dto.setTmp(node.get("fcstValue").asDouble()));
        handlers.put("TMN", node -> dto.setTmn(node.get("fcstValue").asDouble()));
        handlers.put("TMX", node -> dto.setTmx(node.get("fcstValue").asDouble()));
        handlers.put("SNO", node -> dto.setSno(node.get("fcstValue").asText()));
        handlers.put("UUU", node -> dto.setUuu(node.get("fcstValue").asDouble()));
        handlers.put("VVV", node -> dto.setVvv(node.get("fcstValue").asDouble()));
        handlers.put("WAV", node -> dto.setWav(node.get("fcstValue").asDouble()));
        handlers.put("VEC", node -> dto.setVec(node.get("fcstValue").asDouble()));

        for (JsonNode categoryNode : timeList) {
            String category = categoryNode.get("category").asText();
            handlers.getOrDefault(category, node -> {}).accept(categoryNode);
        }

        return dto;
    }


    /**
     * @deprecated batch를 사용하여 날씨 데이터를 저장하는 걸로 바꿔서 이 메서드는 더이상 사용하지 않음.
     */
    @Transactional
    private void saveAllRepeatXYList(List<WeatherShortMain> mainEntityList, List<WeatherShortSub> subEntityList) throws Exception {
        System.out.println("mainEntityList.parallelStream():" + mainEntityList.size());
        mainEntityList.parallelStream().map(entity -> {
                    try {
                        weatherShortMainRepository.save(entity);
                        System.out.println("mainEntityList.parallelStream()" + entity.getId());
                        return entity.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warn("Failed to update entity: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        subEntityList.parallelStream().map(entity -> {
                    try {
                        weatherShortSubRepository.save(entity);
                        System.out.println("subEntityList.parallelStream()" + entity.getId());
                        return entity.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warn("Failed to update entity: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    @Override
    /**
     * 컨트롤러에서 실질적으로 호출하는 메서드로 이 메서드 내에서 xylist를 불러온 뒤 병렬처리를 하여
     * 하나의 격자 x,y 좌표마다 파싱된 날씨 데이터를 mainEntityList, subEntityList에 담아 batch 메서드로 리턴하는 메서드
     *
     * @return weatherShortEntityListDTO에 mainEntityList, subEntityList를 담아 batch 메서드로 리턴, 실패시 예외 처리
     * @throws Exception
     */
    public WeatherShortEntityListDTO getXYListWeatherAllSave() throws Exception {

        List<WeatherShortMain> mainEntityList = new ArrayList<>();
        List<WeatherShortSub> subEntityList = new ArrayList<>();
        WeatherShortRequestDTO weatherShortRequestDTO = new WeatherShortRequestDTO();
        //entity list 병렬처리 + batch
        splitXyList().parallelStream().forEach(xy -> {
            WeatherXY weatherXY = xy;
            weatherShortRequestDTO.setWeatherXY(weatherXY);
            System.out.println("weatherShortRequestDTO==================="+weatherShortRequestDTO);

            try {
                ConcurrentHashMap<String, WeatherXY> weatherXYMap = new ConcurrentHashMap<>();
                weatherXYMap.put("weatherXY", weatherXY);
                for (WeatherShortAllDTO dto : jsonToFcstDateMap(weatherShortRequestDTO)) {
                    //WeatherXY weatherXY = weatherXYRepository.findByWeatherXAndWeatherY(dto.getNx(), dto.getNy());
                    mainEntityList.add(mainDtoToEntity(dto, weatherXYMap.get("weatherXY")));
                    subEntityList.add(subDtoToEntity(dto, weatherXYMap.get("weatherXY")));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("Exception: " + e.getMessage());
            }
        });

        //saveAllRepeatXYList(mainEntityList, subEntityList);
        //System.out.println("mainEntityList===================" + mainEntityList);
        WeatherShortEntityListDTO weatherShortEntityListDTO = new WeatherShortEntityListDTO();
        weatherShortEntityListDTO.setMainEntityList(mainEntityList);
        weatherShortEntityListDTO.setSubEntityList(subEntityList);
        return weatherShortEntityListDTO;

    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    /**
     * 한 번만 저장하면 되는 메서드라 더이상 사용하지 않음.
     */
    //격자 x,y값이 담긴 csv를 postgres내의 테이블에 저장하는 메서드
/*    @Override
    public String readWeatherXYLocation() throws IOException {
        String rootPath = System.getProperty("user.dir");
        BufferedReader reader = new BufferedReader(new FileReader("weatherxy.csv"));
        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            WeatherXY weatherXY = new WeatherXY();
            weatherXY.setWeatherX(Integer.parseInt(data[0]));
            weatherXY.setWeatherY(Integer.parseInt(data[1]));
            weatherXYRepository.save(weatherXY);
        }
        reader.close();

        return "성공";

    }*/


}
