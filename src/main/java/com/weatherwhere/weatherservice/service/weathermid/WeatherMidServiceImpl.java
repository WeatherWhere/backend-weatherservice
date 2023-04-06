package com.weatherwhere.weatherservice.service.weathermid;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.ResultDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private JSONObject customJsonParser(String jsonString) throws ParseException, NullPointerException {
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
        return (JSONObject) jsonItemList.get(0);
    }

    private String changeRegIdForFcst(String regId) {
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
        return regIdForMidFcst;
    }

    @Override
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

    @Override
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

    @Override
    public List<WeatherMidEntity> makeEntityList(List<RegionCodeDTO> regionCodeDTOList, String[] threeToSevenDays, String tmfc) {
        List<WeatherMidEntity> entities = new ArrayList<>();
        Integer dtoListLength = regionCodeDTOList.size();
        Integer daysListLength = threeToSevenDays.length;

        for (int i = 0; i < dtoListLength; i++) {
            try {
                String regId = regionCodeDTOList.get(i).getRegionCode();
                String regName = regionCodeDTOList.get(i).getRegionName();
                String city = regionCodeDTOList.get(i).getCity();
                String regIdForMidFcst = changeRegIdForFcst(regId);

                JSONObject jsonFromMidTa = getWeatherMidTa(regId, tmfc);
                JSONObject jsonFromMidLandFcst = getWeatherMidLandFcst(regIdForMidFcst, tmfc);

                for (int j = 0; j < daysListLength; j++) {
                    WeatherMidDTO dto;
                    String taMin = String.format("taMin%d", j + 3);
                    String taMax = String.format("taMax%d", j + 3);
                    String rnStAm = String.format("rnSt%dAm", j + 3);
                    String rnStPm = String.format("rnSt%dPm", j + 3);
                    String wfAm = String.format("wf%dAm", j + 3);
                    String wfPm = String.format("wf%dPm", j + 3);

                    dto = WeatherMidDTO.builder()
                            .regionCode((String) jsonFromMidTa.get("regId"))
                            .baseTime(threeToSevenDays[j])
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
            } catch (NullPointerException e) {
                e.printStackTrace();
                log.warn("OpenAPi returns null: " + e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
                log.warn("Failed parsing JSON: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("UnExpected Exception: " + e.getMessage());
            }
        }
        return entities;
    }

    @Override
    @Transactional
    public List<WeatherMidCompositeKey> updateWeatherMid(List<WeatherMidEntity> entities) {
        /* 기존 메서드
        List<WeatherMidCompositeKey> ids = new ArrayList<>();
        for (WeatherMidEntity entity : entities) {
            try {
                weatherMidRepository.save(entity);
                ids.add(entity.getId());
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("Failed to update entity: " + e.getMessage());
            }
        }
        return ids;
         */
        // 병렬 처리
        return entities.parallelStream().map(entity -> {
                    try {
                        weatherMidRepository.save(entity);
                        return entity.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warn("Failed to update entity: " + e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public ResultDTO<List<WeatherMidDTO>> getMidForecast(String regionCode) {
        String[] weeks = dateService.getDaysAfterToday(3, 7);
        List<WeatherMidDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < weeks.length; i++) {
            WeatherMidCompositeKey weatherMidCompositeKey = new WeatherMidCompositeKey(regionCode, weeks[i]);
            WeatherMidEntity result = weatherMidRepository.findById(weatherMidCompositeKey).orElseThrow(() -> new NoSuchElementException());
            dtoList.add(entityToDTO(result));
        }
        return ResultDTO.of(HttpStatus.OK.value(), "날씨 중기 예보를 조회하는데 성공하였습니다.", dtoList);
    }
}
