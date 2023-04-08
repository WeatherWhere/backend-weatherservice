package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortMainRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortSubRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherXYRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherShortMainServiceImpl implements WeatherShortMainService {
    private final WeatherShortMainRepository weatherShortMainRepository;

    private final WeatherShortSubRepository weatherShortSubRepository;

    private final WeatherXYRepository weatherXYRepository;


    //xylist 잘게 쪼개는 메서드
    //xylist는 어플리케이션 실행 시 캐시에 저장해놓기 때문에 불필요한 select문을 줄임.
    @Cacheable("xylist")
    @Override
    @Transactional
    public List<WeatherXY> splitXyList() throws Exception {
        List<WeatherXY> xyList = weatherXYRepository.findAll();
        System.out.println("xyList================================"+xyList);
        List<WeatherXY> subList = xyList.subList(0, 100);
        return subList;
    }


    //uri 생성하는 메서드
    private URI makeUri(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = System.getProperty("WEATHER_SHORT_SERVICE_KEY");

        String dataType = "JSON";
        String numOfRows = "302";
        String pageNo = "1";

        String url = String.format("%s?serviceKey=%s&pageNo=%s&numOfRows=%s&dataType=%s&base_date=%s&base_time=%s&nx=%s&ny=%s",
                apiUrl, serviceKey, pageNo, numOfRows, dataType,
                weatherShortRequestDTO.getBaseDate(), weatherShortRequestDTO.getBaseTime(),
                weatherShortRequestDTO.getWeatherXY().getWeatherX(), weatherShortRequestDTO.getWeatherXY().getWeatherY());

        //rest template이 String 문자열을 한 번 더 인코딩 해주는 걸 방지하기 위해 url 객체로 넣음
        URI endUrl = new URI(url);
        return endUrl;

    }


    //공공데이터 api로부터 json값 받아와서 파싱하는 메서드
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

    //파싱한 json값을 fcstDate+fcstTime을 key로 한 dto리스트에 저장(12시간)
    private List<WeatherShortAllDTO> jsonToFcstDateMap(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {

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
        return weatherShortAllDTOList;
    }

    //weatherShortAllDTOList의 시간 당 하나의 dto 요소 저장하는 메서드
    private WeatherShortAllDTO JsonToDTOWeatherShort(WeatherShortRequestDTO weatherShortRequestDTO, List<JsonNode> timeList) {

        JsonNode time = timeList.get(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        WeatherShortAllDTO dto = WeatherShortAllDTO.builder()
                .baseDate(weatherShortRequestDTO.getBaseDate())
                .baseTime(weatherShortRequestDTO.getBaseTime())
                .nx(weatherShortRequestDTO.getWeatherXY().getWeatherX())
                .ny(weatherShortRequestDTO.getWeatherXY().getWeatherY())
                .fcstDateTime(LocalDateTime.parse(time.get("fcstDate").asText() + time.get("fcstTime").asText(), dateFormatter))
                .build();

        for (JsonNode categoryNode : timeList) {
            String category = categoryNode.get("category").asText();
            switch (category) {
                case "POP":
                    dto.setPop(categoryNode.get("fcstValue").asDouble());
                    break;
                case "PCP":
                    dto.setPcp(categoryNode.get("fcstValue").asText());
                    break;
                case "PTY":
                    dto.setPty(categoryNode.get("fcstValue").asDouble());
                    break;
                case "SKY":
                    dto.setSky(categoryNode.get("fcstValue").asDouble());
                    break;
                case "WSD":
                    dto.setWsd(categoryNode.get("fcstValue").asDouble());
                    break;
                case "REH":
                    dto.setReh(categoryNode.get("fcstValue").asDouble());
                    break;
                case "TMP":
                    dto.setTmp(categoryNode.get("fcstValue").asDouble());
                    break;
                case "TMN":
                    dto.setTmn(categoryNode.get("fcstValue").asDouble());
                    break;
                case "TMX":
                    dto.setTmx(categoryNode.get("fcstValue").asDouble());
                    break;
                case "SNO":
                    dto.setSno(categoryNode.get("fcstValue").asText());
                    break;
                case "UUU":
                    dto.setUuu(categoryNode.get("fcstValue").asDouble());
                    break;
                case "VVV":
                    dto.setVvv(categoryNode.get("fcstValue").asDouble());
                    break;
                case "WAV":
                    dto.setWav(categoryNode.get("fcstValue").asDouble());
                    break;
                case "VEC":
                    dto.setVec(categoryNode.get("fcstValue").asDouble());
                    break;
                default:
                    break;
            }
        }
        return dto;
    }


    //dto리스트를 entity리스트로 변환한 뒤 mainEntityList에 저장
    @Transactional
    private void weatherShortDtoToEntity(WeatherShortRequestDTO weatherShortRequestDTO, List<WeatherShortMain> mainEntityList, List<WeatherShortSub> subEntityList) throws Exception {


        for (WeatherShortAllDTO dto : jsonToFcstDateMap(weatherShortRequestDTO)) {
            //WeatherXY weatherXY = weatherXYRepository.findByWeatherXAndWeatherY(dto.getNx(), dto.getNy());
            mainEntityList.add(mainDtoToEntity(dto, weatherShortRequestDTO.getWeatherXY()));
            subEntityList.add(subDtoToEntity(dto, weatherShortRequestDTO.getWeatherXY()));
        }

    }


    @Transactional
    //entity리스트를 병렬처리하면서 db에 save하는 메서드
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

    //모든 xy리스트에 대한 값 저장하는 메서드
    @Override
    public String getXYListWeatherAllSave(WeatherShortRequestDTO weatherShortRequestDTO, List<WeatherShortMain> mainEntityList, List<WeatherShortSub> subEntityList) throws Exception {

        splitXyList().stream().map(xy -> {
                    WeatherXY weatherXY = xy;
                    weatherShortRequestDTO.setWeatherXY(weatherXY);

                    System.out.println("weatherShortRequestDTO==================="+weatherShortRequestDTO);
                    try {
                        weatherShortDtoToEntity(weatherShortRequestDTO, mainEntityList, subEntityList);
                        return "성공";
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        saveAllRepeatXYList(mainEntityList, subEntityList);
        System.out.println("mainEntityList===================" + mainEntityList);
        return "성공";

    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

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