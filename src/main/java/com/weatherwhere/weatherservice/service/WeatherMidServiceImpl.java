package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.WeatherMidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherMidServiceImpl implements WeatherMidService {
    private final WeatherMidRepository weatherMidRepository;

    private URI makeUriForWeatherMid(String apiUrl, String serviceKey, String pageNo, String numOfRows, String dataType,
                                     String regId, String tmFc) {
        // UriComponentsBuilder는 URI를 동적으로 생성해주는 클래스로, 파라미터 값 지정이나 변경이 쉽다.
        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("dataType", dataType)
                .queryParam("regId", regId)
                .queryParam("tmFc", tmFc)
                .encode()
                .build()
                .toUri();
        return uri;
    }
    public ResponseEntity<String> getWeatherMidTa(String regId, String tmFc) {
        // 예보 구역코드와, 발표 시각은 변수어야 한다. - 매개변수로 받음 -
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA==";
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";


        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(new HttpHeaders());
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, dataType, numOfRows, pageNo, regId, tmFc);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return result;
    }

    public ResponseEntity<String> getWeatherMidLandFcst(String regId, String tmFc) {
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA==";
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(new HttpHeaders());
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, dataType, numOfRows, pageNo, regId, tmFc);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return result;
    }


    public Long register(WeatherMidDTO dto) {
        // 파라미터가 제대로 넘어오는지 확인
        log.info("삽입 데이터:" + dto.toString());

        // repository의 메서드 매개변수로 변경
        WeatherMidEntity entity = dtoToEntity(dto);
        // 이 시점에는 entity에 mid_term_forecast_id와 regDate, modDate는 없고,
        // save를 할 때 설정한 내역을 가지고 데이터를 설정
        weatherMidRepository.save(entity);
        return entity.getMid_term_forecast_id();
    }
}
