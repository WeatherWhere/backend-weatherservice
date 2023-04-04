package com.weatherwhere.weatherservice.service.weathermid;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.weathermid.WeatherMidRepository;
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
import java.util.NoSuchElementException;

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
        String serviceKey = System.getProperty("WEATHER_MID_SERVICE_KEY");
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
        String serviceKey = System.getProperty("WEATHER_MID_SERVICE_KEY");
        String dataType = "JSON";
        String numOfRows = "1000";
        String pageNo = "1";

        RestTemplate restTemplate = new RestTemplate();
        URI uri = makeUriForWeatherMid(apiUrl, serviceKey, pageNo, numOfRows, dataType, regId, tmFc);
        String jsonString = restTemplate.getForObject(uri, String.class);
        JSONObject result = customJsonParser(jsonString);
        return result;
    }

    public List<WeatherMidEntity> makeEntityList(JSONObject jsonFromMidTa, JSONObject jsonFromMidLandFcst,
                                                 String[] daysAfterToday, String regName, String city) {
        List<WeatherMidEntity> entities = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            WeatherMidDTO dto;
            String taMin = String.format("taMin%d", i + 3);
            String taMax = String.format("taMax%d", i + 3);
            String rnStAm = String.format("rnSt%dAm", i + 3);
            String rnStPm = String.format("rnSt%dPm", i + 3);
            String wfAm = String.format("wf%dAm", i + 3);
            String wfPm = String.format("wf%dPm", i + 3);

            dto = WeatherMidDTO.builder()
                    .regionCode((String) jsonFromMidTa.get("regId"))
                    .baseTime(daysAfterToday[i])
                    .regionName(regName)
                    .city(city)
                    .tmn((Long) jsonFromMidTa.get(taMin))
                    .tmx((Long) jsonFromMidTa.get(taMax))
                    .rAm((Long) jsonFromMidLandFcst.get(rnStAm))
                    .rPm((Long) jsonFromMidLandFcst.get(rnStPm))
                    .wAm((String) jsonFromMidLandFcst.get(wfAm))
                    .wPm((String) jsonFromMidLandFcst.get(wfPm))
                    .build();
            entities.add(dtoToEntity(dto));
        }
        return entities;
    }

    @Transactional
    public List<WeatherMidCompositeKey> updateWeatherMid(RegionCodeDTO regionCodeDTO, String tmfc) {
        // 새로 만들어진 튜플의 기본키를 리스트로 리턴
        List<WeatherMidCompositeKey> ids = new ArrayList<>();
        String regId = regionCodeDTO.getRegionCode();
        String regName = regionCodeDTO.getRegionName();
        String city = regionCodeDTO.getCity();

        try {
            // 중기 예보 API 호출
            JSONObject jsonFromMidTa = getWeatherMidTa(regId, tmfc);
            String prefix = regId.substring(0, 4);
            String regIdForMidFcst;

            // 지역코드를 기반으로 기상예보 구역 코드 생성
            if (prefix.equals("11B0") || prefix.equals("11B1") || prefix.equals("11A0") || prefix.equals("11B2")) {
                // 서울, 인천, 경기도
                // "11A0-" 은 백령도로 인천시이다.
                regIdForMidFcst = "11B00000";
            } else if (prefix.equals("21F1") || prefix.equals("21F2")) {
                // 전라북도
                regIdForMidFcst = "11F10000";
            } else {
                // 이 외에는 앞 4글자 + "0000"
                regIdForMidFcst = prefix + "0000";
            }

            // 육상 예보 호출
            JSONObject jsonFromMidFcst = getWeatherMidLandFcst(regIdForMidFcst, tmfc);

            // 3일부터 7일후까지의 날짜 배열 받기
            String[] daysArray = dateService.getDaysAfterToday(3, 7);

            // 중기 예보, 육상 예보, 날짜를 매개변수로 entity 배열을 받아옴.
            List<WeatherMidEntity> entities = makeEntityList(jsonFromMidTa, jsonFromMidFcst, daysArray, regName, city);

            for (WeatherMidEntity entity : entities) {
                weatherMidRepository.save(entity);
                ids.add(entity.getId());
            }

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Wrong regIdForMidTa or regIdForMidFcst: " + regId + e.getMessage());
        } catch (Exception e) {
            // 예외가 발생하면 로그를 출력하고 계속 진행한다.
            // 이때, catch 블록 안에서는 트랜잭션이 롤백되지 않기 때문에,
            // 다른 데이터는 여전히 저장될 수 있다.
            System.out.println("Failed to update entity: " + e.getMessage());
        }

        return ids;
    }

    public List<WeatherMidDTO> getMidForecast(String regionCode) {
        String[] weeks = dateService.getDaysAfterToday(3, 7);
        List<WeatherMidDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < weeks.length; i++) {
            WeatherMidCompositeKey weatherMidCompositeKey = new WeatherMidCompositeKey(regionCode, weeks[i]);
            WeatherMidEntity result = weatherMidRepository.findById(weatherMidCompositeKey).orElseThrow(() -> new NoSuchElementException());
            dtoList.add(entityToDTO(result));
        }
        return dtoList;
    }
}
