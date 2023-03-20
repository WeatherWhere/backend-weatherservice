package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.WeatherMidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        // 중기 예보 조회와 육상 정보 조회의 요청 URI 형태는 동일하므로 공통 로직을 분리
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

    private Object jsonParser(String jsonString) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

        // 가장 큰 JSON 객체 response 가져오기
        JSONObject jsonResponse = (JSONObject) jsonObject.get("response");

        // response 안의 body 부분 파싱
        JSONObject jsonBody = (JSONObject) jsonResponse.get("body");

        // body 안의 items 파싱
        JSONObject jsonItems = (JSONObject) jsonBody.get("items");

        // items 안의 item은 배열.
        JSONArray jsonItemList = (JSONArray) jsonItems.get("item");

        // item 배열의 첫 번쨰 object를 리턴
        System.out.println(jsonItemList.get(0).getClass().getName());
        return jsonItemList.get(0);
    }

    public Object getWeatherMidTa(String regId, String tmFc) throws ParseException {
        // 예보 구역코드와, 발표 시각은 변수어야 한다. - 매개변수로 받음 -
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA==";
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        RestTemplate restTemplate = new RestTemplate();
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, pageNo, numOfRows, dataType, regId, tmFc);

        String jsonString = restTemplate.getForObject(uri, String.class);
        Object result = jsonParser(jsonString);
        return result;
    }

    public Object getWeatherMidLandFcst(String regId, String tmFc) throws ParseException {
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA==";
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        RestTemplate restTemplate = new RestTemplate();
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, pageNo, numOfRows, dataType, regId, tmFc);
        String jsonString = restTemplate.getForObject(uri, String.class);
        Object result = jsonParser(jsonString);
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
