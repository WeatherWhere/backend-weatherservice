package com.weatherwhere.weatherservice.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class WeatherApiWebClientService {

    public Object[] fluxService() {

        String apiURL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String authKey = System.getenv("WEATHER_SHORT_SERVICE_KEY"); // 본인 서비스 키 입력

        String nx = "69";
        String ny = "100";
        String baseDate = "20230314";
        String baseTime = "0500";
        String dataType = "JSON";
        String numOfRows = "519";
        String pageNo = "1";

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiURL);
        // 인코딩 모드 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient client = WebClient.builder()
                .baseUrl(apiURL)
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<Object[]> mono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",authKey)
                        .queryParam("numOfRows", numOfRows)
                        .queryParam("pageNo", pageNo)
                        .queryParam("dataType", dataType)
                        .queryParam("base_date", baseDate)
                        .queryParam("base_time", baseTime)
                        .queryParam("nx", nx)
                        .queryParam("ny", ny)
                        .build())
                .retrieve()
                .bodyToMono(Object[].class).log();
        Object[] objects = mono
                .share().block();

        return objects;
    }




}
