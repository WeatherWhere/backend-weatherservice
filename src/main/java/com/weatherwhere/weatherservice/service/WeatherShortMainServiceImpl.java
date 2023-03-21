package com.weatherwhere.weatherservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherXY;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
import com.weatherwhere.weatherservice.repository.WeatherShortMainRepository;
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

    @Override
    public JsonNode weatherShortJsonParsing(String baseDate, String baseTime, String nx, String ny) throws JsonProcessingException, URISyntaxException {
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


    //단기예보 api 받아서 dto에 저장한 뒤 entity로 변환하고 db에 save하는 서비스
    //override를 안해도 오류가 발생하지 않지만 해야 컴파일할떄 버그를 쉽게 찾을 수 있음.
    //impl의 소스코드와 연결되어 있다는 걸 뜻함.
    @Override
    public List<WeatherShortMainDto> getWeatherShortDto(String nx, String ny, String baseDate, String baseTime) throws URISyntaxException, JsonProcessingException {

        List<WeatherShortMainDto> weatherShortMainDtoList = StreamSupport.stream(weatherShortJsonParsing(baseDate, baseTime, nx, ny).spliterator(), false)
                //예보날짜+시간을 key값으로 함
                .collect(Collectors.groupingBy(time -> time.get("fcstTime").asText() + time.get("fcstDate").asText()))
                .values().stream()
                //예보날짜+시간순으로 정렬
                .sorted(Comparator.comparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstDate").asText())
                        .thenComparing((List<JsonNode> timeList) -> timeList.get(0).get("fcstTime").asText()))
                .map(timeList -> {
                    JsonNode time = timeList.get(0);
                    WeatherShortMainDto dto = new WeatherShortMainDto();
                    dto.setWeatherX(Integer.parseInt(nx));
                    dto.setWeatherY(Integer.parseInt(ny));
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
                                dto.setPcp(categoryNode.get("fcstValue").asDouble());
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
                            default:
                                break;
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        //dto리스트를 entity리스트로 변환하는 부분
        List<WeatherShortMain> entityList = new ArrayList<>();
        for (WeatherShortMainDto dto : weatherShortMainDtoList) {

            //엔티티에 fcstdate와 fcsttime이 동일한 값이 존재하는지 판별하는 메서드
            WeatherShortMain existingEntity = weatherShortMainRepository.findByFcstDateAndFcstTime(dto.getFcstDate(), dto.getFcstTime());
            //해당하는 fcsttime+fcstdate가 존재할 경우 엔티티 업데이트
            if (existingEntity != null) {
                existingEntity.update(dto);
                entityList.add(existingEntity);
            } else { //존재하지 않을 경우 엔티티 새로 생성
                WeatherShortMain entity = dtoToEntity(dto);
                entityList.add(entity);
            }
        }
        //db에 저장
        weatherShortMainRepository.saveAll(entityList);
        System.out.println(weatherShortMainDtoList);

        return weatherShortMainDtoList;

    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @Autowired
    private WeatherXYRepository weatherXYRepository;

    //격자 x,y값이 담긴 csv를 postgres내의 테이블에 저장하는 메서드
    public String readWeatherXYLocation() throws IOException {
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

    }


}