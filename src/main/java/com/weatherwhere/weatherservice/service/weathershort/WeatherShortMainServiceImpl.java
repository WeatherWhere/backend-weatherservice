package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.URISyntaxException;
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
                weatherShortRequestDTO.getNx(), weatherShortRequestDTO.getNy());

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

    //파싱한 json값을 dto리스트에 저장하는 메서드
    //override를 안해도 오류가 발생하지 않지만 해야 컴파일할떄 버그를 쉽게 찾을 수 있음.
    //impl의 소스코드와 연결되어 있다는 걸 뜻함.
    private List<WeatherShortAllDTO> getWeatherShortDto(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {
        JsonNode itemNode = weatherShortJsonParsing(weatherShortRequestDTO);
        List<WeatherShortAllDTO> weatherShortAllDTOList = StreamSupport.stream(itemNode.spliterator(), false)
                //예보날짜+시간을 key값으로 함
                .collect(Collectors.groupingBy(time -> time.get("fcstTime").asText() + time.get("fcstDate").asText()))
                .values().stream()
                //예보날짜+시간순으로 정렬
                .sorted(Comparator.comparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstDate").asText())
                        .thenComparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstTime").asText()))
                .map(timeList -> {
                    WeatherXY weatherXY = weatherXYRepository.findByWeatherXAndWeatherY(weatherShortRequestDTO.getNx(), weatherShortRequestDTO.getNy());
                    JsonNode time = timeList.get(0);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

                    WeatherShortAllDTO dto = WeatherShortAllDTO.builder()
                            .baseDate(weatherShortRequestDTO.getBaseDate())
                            .baseTime(weatherShortRequestDTO.getBaseTime())
                            .weatherXY(weatherXY)
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
                })
                .collect(Collectors.toList());
        return weatherShortAllDTOList;
    }


    @Transactional
    //dto리스트를 entity리스트로 변환한 뒤 db에 save하는 메서드
    //컨트롤러에서 최종적으로 이 service를 호출함.
    @Override
    public String saveWeatherShortEntity(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {
        //dto리스트를 entity리스트로 변환하는 부분
        List<WeatherShortMain> mainEntityList = new ArrayList<>();
        List<WeatherShortSub> subEntityList = new ArrayList<>();
        Integer[] nxList = {53, 54, 54, 54};
        Integer[] nyList = {125, 123, 126, 129};
        String nx = "69";
        String ny = "100";
        System.out.println("xylist=====================================================================");
        List<Object[]> xyList = weatherXYRepository.findAllNxAndNy();
        List<Object[]> subList = xyList.subList(1600, 1603);

        for (Object[] xy : subList) {
            Integer nx2 = (Integer) xy[0];
            Integer ny2 = (Integer) xy[1];
            // nx, ny 값 사용
            weatherShortRequestDTO.setNx(nx2);
            weatherShortRequestDTO.setNy(ny2);
            try {
                for (WeatherShortAllDTO dto : getWeatherShortDto(weatherShortRequestDTO)) {
                    //엔티티에 fcstdate, fcsttime, 격자x,y값이 동일한 엔티티가 존재하는지 판별하는 메서드
                    WeatherShortMain mainExistingEntity = weatherShortMainRepository.findByFcstDateTimeAndWeatherXY(dto.getFcstDateTime(), dto.getWeatherXY());
                    WeatherShortSub subExistingEntity = weatherShortSubRepository.findByFcstDateTimeAndWeatherXY(dto.getFcstDateTime(), dto.getWeatherXY());
                    System.out.println("mainExistingEntity=========================================================="+mainExistingEntity);

                    System.out.println(dto.getWeatherXY());
                    if (mainExistingEntity != null) {
                        mainExistingEntity.update(dto);
                        subExistingEntity.update(dto);
                        mainEntityList.add(mainExistingEntity);
                        subEntityList.add(subExistingEntity);
                    } else { //존재하지 않을 경우 엔티티 새로 생성
                        WeatherShortMain mainEntity = mainDtoToEntity(dto);
                        WeatherShortSub subEntity = subDtoToEntity(dto);
                        mainEntityList.add(mainEntity);
                        subEntityList.add(subEntity);

                    }
                }
                weatherShortMainRepository.saveAll(mainEntityList);
                weatherShortSubRepository.saveAll(subEntityList);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new Exception("오류 발생", e);
            }


        }
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