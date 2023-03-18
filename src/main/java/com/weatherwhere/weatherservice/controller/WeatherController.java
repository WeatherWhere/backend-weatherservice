package com.weatherwhere.weatherservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weatherwhere.weatherservice.dto.TestDto;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
import com.weatherwhere.weatherservice.service.WeatherApiWebClientService;
//import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherController {

    @GetMapping("test")
    public String test(){
        return "test";
    }


/*

    //private final WeatherShortApiService weatherShortApiService;

    private final WeatherRestTemService weatherRestTemService;

*/
/*    @GetMapping("/short-test")
    public WeatherShortMainDto weathertest(WeatherShortMainDto weatherShortMainDto) throws Exception{
        System.out.println("컨트롤러 서비스:"+weatherShortApiService.WeatherJsonParsing(weatherShortMainDto));
        return weatherShortApiService.WeatherJsonParsing(weatherShortMainDto);
    }*//*



    @GetMapping("/rest-tem")
    public List<WeatherShortMainDto> weatherShortMainDtoList() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException, ParseException {

        return weatherRestTemService.getWeatherShortDto();
    }


    @GetMapping("/test")
    public String test() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String key1 = System.getenv("WEATHER_SHORT_SERVICE_KEY");
        String base_date = "20230316";
        String base_time = "0500";
        String dataType = "JSON";
        String numOfRows = "500";
        String pageNo = "1";
        String nx = "69";
        String ny = "100";
        System.out.println("인코딩 전:" + key1);


        String url = String.format("%s?serviceKey=%s&pageNo=%s&numOfRows=%s&dataType=%s&base_date=%s&base_time=%s&nx=%s&ny=%s",
                apiUrl, key1, pageNo, numOfRows, dataType, base_date,base_time, nx, ny);

        URI url2 = new URI(url);
        System.out.println("최종 url:"+ url);
        String response = restTemplate.getForObject(url2, String.class);

        System.out.println("response:" + response);

        return response;
    }


    */
/*
    @GetMapping("/webclient")
    public Object[] getTweetsNonBlocking() {
        System.out.println("서비스값:" +weatherApiWebClientService.fluxService());
        return weatherApiWebClientService.fluxService();
    }*//*



*/


}
