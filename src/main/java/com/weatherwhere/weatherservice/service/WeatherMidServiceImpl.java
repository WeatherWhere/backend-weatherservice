package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.WeatherMidRepository;
import com.weatherwhere.weatherservice.service.date.DateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherMidServiceImpl implements WeatherMidService {
    private final WeatherMidRepository weatherMidRepository;
    private final DateService dateService;

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

    private JSONObject customJsonParser(String jsonString) throws ParseException {
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
        return (JSONObject) jsonItemList.get(0);
    }

    public JSONObject getWeatherMidTa(String regId, String tmFc) throws ParseException {
        // 예보 구역코드와, 발표 시각은 변수어야 한다. - 매개변수로 받음 -
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA==";
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        RestTemplate restTemplate = new RestTemplate();
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, pageNo, numOfRows, dataType, regId, tmFc);

        String jsonString = restTemplate.getForObject(uri, String.class);
        JSONObject result = customJsonParser(jsonString);
        return result;
    }

    public JSONObject getWeatherMidLandFcst(String regId, String tmFc) throws ParseException {
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
        String serviceKey = "XiXQig6ZMt9WhFnz7w2pl78HnvEb4h5S1s3n51BpoJU5L064VCaM1iT8DUUrx8Qta9OPr3nnm88UtKukLSf0xA==";
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        RestTemplate restTemplate = new RestTemplate();
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, pageNo, numOfRows, dataType, regId, tmFc);
        String jsonString = restTemplate.getForObject(uri, String.class);
        JSONObject result = customJsonParser(jsonString);
        return result;
    }

    public List<WeatherMidDTO> makeDTOList(JSONObject jsonFromMidTa, JSONObject jsonFromMidLandFcst, String[] daysAfterToday) {
        List<WeatherMidDTO> dtoList = new ArrayList<WeatherMidDTO>();

        for (int i = 0; i < 5; i++) {
            WeatherMidDTO dto;
            String taMin = String.format("taMin%d", i + 3);
            String taMax = String.format("taMax%d", i + 3);
            String rnStAm = String.format("rnSt%dAm", i + 3);
            String rnStPm = String.format("rnSt%dPm", i + 3);
            String wfAm = String.format("wf%dAm", i + 3);
            String wfPm = String.format("wf%dPm", i + 3);

            dto = WeatherMidDTO.builder()
                    .baseTime(daysAfterToday[i])
                    .tmn((Long) jsonFromMidTa.get(taMin))
                    .tmx((Long) jsonFromMidTa.get(taMax))
                    .regionCode((String) jsonFromMidTa.get("regId"))
                    .rAm((Long) jsonFromMidLandFcst.get(rnStAm))
                    .rPm((Long) jsonFromMidLandFcst.get(rnStPm))
                    .wAm((String) jsonFromMidLandFcst.get(wfAm))
                    .wPm((String) jsonFromMidLandFcst.get(wfPm))
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Long register(WeatherMidDTO dto) {
        // 파라미터가 제대로 넘어오는지 확인
        log.info("삽입 데이터:" + dto.toString());

        // repository의 메서드 매개변수로 변경
        WeatherMidEntity entity = dtoToEntity(dto);
        // 이 시점에는 entity에 mid_term_forecast_id와 regDate, modDate는 없고,
        // save를 할 때 설정한 내역을 가지고 데이터를 설정
        weatherMidRepository.save(entity);
        return entity.getMidTermForecastId();
    }

    @Transactional
    public List<Long> updateWeatherMid(String regId, String tmfc) throws ParseException{
        JSONObject jsonFromMidTa = getWeatherMidTa(regId, tmfc);

        String prefix = regId.substring(0, 4);
        String regIdForMidFcst;

        if (prefix.equals("11B0") || prefix.equals("11B1") || prefix.equals("11A0")) {
            // 서울, 인천, 경기도
            // "11A0-" 은 백령도로 인천시이다.
            regIdForMidFcst = "11B00000";
        } else if (prefix.equals("1100")) {
            // "1100-" 은 울릉도, 독도로 경상북도
            regIdForMidFcst = "11H10000";
        } else if (prefix.equals("21F1") || prefix.equals("21F2")) {
            // 전라북도
            regIdForMidFcst = "11F10000";
        } else {
            // 이 외에는 앞 4글자 + "0000"
            regIdForMidFcst = prefix + "0000";
        }
        System.out.println(regIdForMidFcst);

        JSONObject jsonFromMidFcst = getWeatherMidLandFcst(regIdForMidFcst, tmfc);
        // 3일부터 7일후까지의 날짜 배열 받기
        String[] daysArray = dateService.getDaysAfterToday(3, 7);
        List<WeatherMidDTO> dtoList = makeDTOList(jsonFromMidTa, jsonFromMidFcst, daysArray);

        List<Long> ids = new ArrayList<>();
        // 새로 만들어진 튜플의 기본키를 리스트로 리턴
        for(WeatherMidDTO dto: dtoList) {
            ids.add(register(dto));
        }
        return ids;
    }
}
