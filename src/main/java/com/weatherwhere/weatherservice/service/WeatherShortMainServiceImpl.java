package com.weatherwhere.weatherservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.WeatherXY;
import com.weatherwhere.weatherservice.dto.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.repository.WeatherShortMainRepository;
import com.weatherwhere.weatherservice.repository.WeatherShortSubRepository;
import com.weatherwhere.weatherservice.repository.WeatherXYRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class WeatherShortMainServiceImpl implements WeatherShortMainService {
    @Autowired
    private WeatherShortMainRepository weatherShortMainRepository;

    @Autowired
    private WeatherShortSubRepository weatherShortSubRepository;

    //공공데이터 api로부터 json값 받아와서 파싱하는 메서드
    private JsonNode weatherShortJsonParsing(String nx, String ny, String baseDate, String baseTime) throws JsonProcessingException, URISyntaxException {
        //http 통신방식 = rest template
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = System.getProperty("WEATHER_SHORT_SERVICE_KEY");

        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        String url = String.format("%s?serviceKey=%s&pageNo=%s&numOfRows=%s&dataType=%s&base_date=%s&base_time=%s&nx=%s&ny=%s",
                apiUrl, serviceKey, pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny);

        //rest template이 String 문자열을 한 번 더 인코딩 해주는 걸 방지하기 위해 url 객체로 넣음
        URI endUrl = new URI(url);
        String response = restTemplate.getForObject(endUrl, String.class);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonNode 사용해서 response의 값들 tree구조로 읽음
        JsonNode rootNode = objectMapper.readTree(response);

        //item 값만 가져와서 itemNode에 저장
        JsonNode itemNode = rootNode.path("response").path("body").path("items").path("item");
        return itemNode;
    }


    //파싱한 json값을 dto리스트에 저장하는 메서드
    //override를 안해도 오류가 발생하지 않지만 해야 컴파일할떄 버그를 쉽게 찾을 수 있음.
    //impl의 소스코드와 연결되어 있다는 걸 뜻함.
    private List<WeatherShortAllDTO> getWeatherShortDto(String nx, String ny, String baseDate, String baseTime) throws URISyntaxException, JsonProcessingException {

        List<WeatherShortAllDTO> weatherShortAllDTOList = StreamSupport.stream(weatherShortJsonParsing(nx, ny, baseDate, baseTime).spliterator(), false)
                //예보날짜+시간을 key값으로 함
                .collect(Collectors.groupingBy(time -> time.get("fcstTime").asText() + time.get("fcstDate").asText()))
                .values().stream()
                //예보날짜+시간순으로 정렬
                .sorted(Comparator.comparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstDate").asText())
                        .thenComparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstTime").asText()))
                .map(timeList -> {
                    WeatherXY weatherXY = weatherXYRepository.findByWeatherXAndWeatherY(Integer.parseInt(nx), Integer.parseInt(ny));
                    JsonNode time = timeList.get(0);
                    WeatherShortAllDTO dto = new WeatherShortAllDTO();
                    dto.setWeatherXY(weatherXY);
                    dto.setBaseDate(baseDate);
                    dto.setBaseTime(baseTime);
                    dto.setFcstDate(time.get("fcstDate").asText());
                    dto.setFcstTime(time.get("fcstTime").asText());
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

    //dto리스트를 entity리스트로 변환한 뒤 db에 save하는 메서드
    //컨트롤러에서 최종적으로 이 service를 호출함.
    @Override
    public String getWeatherShortEntity(String nx, String ny, String baseDate, String baseTime) throws URISyntaxException, JsonProcessingException {
        //dto리스트를 entity리스트로 변환하는 부분
        List<WeatherShortMain> mainEntityList = new ArrayList<>();
        List<WeatherShortSub> subEntityList = new ArrayList<>();
        for (WeatherShortAllDTO dto : getWeatherShortDto(nx, ny, baseDate, baseTime)) {
            WeatherXY weatherXY = weatherXYRepository.findByWeatherXAndWeatherY(Integer.parseInt(nx), Integer.parseInt(ny));
            //엔티티에 fcstdate와 fcsttime이 동일한 값이 존재하는지 판별하는 메서                mainExistingEntity.setWeatherXY(weatherXY);드
            WeatherShortMain mainExistingEntity = weatherShortMainRepository.findByFcstDateAndFcstTime(dto.getFcstDate(), dto.getFcstTime());
            //엔티티에 fcstdate와 fcsttime이 동일한 값이 존재하는지 판별하는 메서드
            WeatherShortSub subExistingEntity = weatherShortSubRepository.findByFcstDateAndFcstTime(dto.getFcstDate(), dto.getFcstTime());
            //해당하는 fcsttime+fcstdate가 존재할 경우 엔티티 업데이트
            if (mainExistingEntity != null || subExistingEntity != null) {
                mainExistingEntity.update(dto);
                mainExistingEntity.setWeatherXY(weatherXY);
                subExistingEntity.update(dto);
                subExistingEntity.setWeatherXY(weatherXY);
                mainEntityList.add(mainExistingEntity);
                subEntityList.add(subExistingEntity);
            } else { //존재하지 않을 경우 엔티티 새로 생성
                WeatherShortMain mainEntity = mainDtoToEntity(dto);
                mainEntity.setWeatherXY(weatherXY);
                WeatherShortSub subEntity = subDtoToEntity(dto);
                subEntity.setWeatherXY(weatherXY);
                mainEntityList.add(mainEntity);
                subEntityList.add(subEntity);

            }
        }
        //db에 저장
        weatherShortMainRepository.saveAll(mainEntityList);
        weatherShortSubRepository.saveAll(subEntityList);
        //System.out.println(entityList);

        return "성공";
    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @Autowired
    private WeatherXYRepository weatherXYRepository;

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