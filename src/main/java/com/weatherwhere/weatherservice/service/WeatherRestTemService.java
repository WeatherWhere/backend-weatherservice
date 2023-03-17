package com.weatherwhere.weatherservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
import jakarta.persistence.criteria.Root;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class WeatherRestTemService {

    public List<WeatherShortMainDto> getWeatherShortDto() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException, ParseException {
        //http 통신방식 = rest template
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = System.getenv("WEATHER_SHORT_SERVICE_KEY");
        String base_date = "20230316";
        String base_time = "0500";
        String dataType = "JSON";
        String numOfRows = "809";
        String pageNo = "1";
        String nx = "69";
        String ny = "100";
        String url = String.format("%s?serviceKey=%s&pageNo=%s&numOfRows=%s&dataType=%s&base_date=%s&base_time=%s&nx=%s&ny=%s",
                apiUrl, serviceKey, pageNo, numOfRows, dataType, base_date, base_time, nx, ny);

        //rest template이 String 문자열을 한 번 더 인코딩 해주는 걸 방지하기 위해 url 객체로 넣음
        URI endUrl = new URI(url);

        String response = restTemplate.getForObject(endUrl, String.class);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // JsonNode 사용해서 response의 값들 tree구조로 읽음
        JsonNode rootNode = objectMapper.readTree(response);
        //item 값만 가져와서 itemNode에 저장
        JsonNode itemNode = rootNode.path("response").path("body").path("items").path("item");
        System.out.println("itemNode:" + itemNode);

        int limit = 10;

        //WeatherShortMainDto를 리스트로 변환
        List<WeatherShortMainDto> weatherShortMainDtoList = new ArrayList<>();

        for (JsonNode item : itemNode) {
            Integer weather_x = item.get("nx").asInt();
            Integer weather_y = item.get("ny").asInt();
            String fcst_date = item.get("fcstDate").asText();
            String fcst_time = item.get("fcstTime").asText();

            // WeatherDTO 객체 생성
            WeatherShortMainDto weatherShortMainDto = new WeatherShortMainDto();
            weatherShortMainDto.setBase_date(base_date);
            weatherShortMainDto.setBase_time(base_time);
            weatherShortMainDto.setWeather_x(weather_x);
            weatherShortMainDto.setWeather_y(weather_y);

            String category = item.get("category").asText();

            double value = item.get("fcstValue").asDouble();

            switch (category) {
                case "POP":
                    weatherShortMainDto.setPop(value);
                    break;

            }

        }


    }


}

}









