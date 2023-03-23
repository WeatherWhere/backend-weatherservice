package com.weatherwhere.weatherservice.controller;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;


@RestController
@RequestMapping("/weather")
public class WeatherMidController {
    @GetMapping("/test")
    public ResponseEntity<Object> test() throws UnsupportedEncodingException, URISyntaxException {
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA%3D%3D";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "JSON", Charset.forName("UTF-8")));

        String pageNo = "1"; // 페이지 번호
        String numOfRows = "10"; // 한 페이지 결과 수
        String dataType = "JSON"; // 응답자료 형식
        String regId = "11B00000"; // 예보 구역 코드
        String tmFc = "202303210600"; // 발표 시각

        StringBuilder urlBuilder = new StringBuilder(apiUrl);

        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(tmFc, "UTF-8"));

        URI uri = new URI(urlBuilder.toString());

        // ParameterizedTypeReference 인자를 잘 활용하자.
        ResponseEntity<Object> result
                = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {
                }
        );
        System.out.println(result);
        return result;
    }
}
