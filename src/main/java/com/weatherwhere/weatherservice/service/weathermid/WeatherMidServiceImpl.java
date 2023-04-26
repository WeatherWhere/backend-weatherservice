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

    /**
     * 중기 예보 조회, 육상 정보 조회 openAPI를 호출하기 위해 URI를 생성하는 메서드
     *
     * @param apiUrl openAPI url
     * @param serviceKey oepnAPI에 등록한 계정 key값
     * @param pageNo 페이지 수
     * @param numOfRows 행 수
     * @param dataType 데이터 포맷
     * @param regId 지역 코드
     * @param tmFc 발표 시간
     *
     * @return 여러 param을 기준으로 URI를 만들어 return
     */
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

    /**
     * openAPI로부터 받아온 JSON 문자열을 JSON으로 파싱하는 메서드로 JSONObject로 리턴
     *
     * @param jsonString openAPI를 통해 받아오는 JSON 형태의 문자열
     *
     * @return response.body.items.item의 첫 번째 object를 JSONObject로 파싱하여 리턴
     * @throws ParseException 파싱할 때 발생하는 예외
     * @throws NullPointerException 값이 없을 때 발생하는 예외
     */
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

    /**
     * 중기 예보 조회 openAPI에 전달하는 '지역코드' 매개변수를 육상 예보 조회 openAPI에 매개변수로 전달할 적절한 '구역 코드'로 변환하여 리턴
     *
     * @param regId 중기 예보 조회 openAPI에 전달하는 지역코드
     * @return regId 지역에 해당하는 구역 코드를 regIdForMidFcst로 리턴
     */
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
    /**
     * 중기 예보 조회 openAPI를 호출하여 JSONObject를 리턴하는 메서드
     *
     * @return result
     */
    public JSONObject getWeatherMidTa(String regId, String tmFc) throws ParseException, NullPointerException {
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
    /**
     * 육상 예보 조회 openAPI를 호출하여 JSONObject를 리턴하는 메서드
     *
     * @return result
     */
    public JSONObject getWeatherMidLandFcst(String regId, String tmFc) throws ParseException, NullPointerException {
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
    /**
     * DB에 저장할 중기 날씨 예보 데이터들을 List<WeatherMidEntity>로 리턴
     *
     * @param regionCodeDTOList csv 파일로부터 읽어온 지역 데이터
     * @param threeToSevenDays 3일 후부터 7일 후까지의 날짜 문자열을 담은 리스트
     * @param tmfc 발표 날짜
     * @return List<WeatherMidEntity>
     */
    public List<WeatherMidEntity> makeEntityList(List<RegionCodeDTO> regionCodeDTOList, String[] threeToSevenDays, String tmfc) {
        List<WeatherMidEntity> entities = new ArrayList<>();
        Integer dtoListLength = regionCodeDTOList.size();
        Integer daysListLength = threeToSevenDays.length;
        String regId = "";
        String regIdForMidFcst = "";
        String city = "";
        String regName = "";

        for (int i = 0; i < dtoListLength; i++) {
            try {
                regId = regionCodeDTOList.get(i).getRegionCode();
                regName = regionCodeDTOList.get(i).getRegionName();
                city = regionCodeDTOList.get(i).getCity();
                regIdForMidFcst = changeRegIdForFcst(regId);

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
                log.warn("OpenAPi returns null: " + regId + " , " + regIdForMidFcst + e.getMessage());
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

    /**
     * @deprecated Spring Batch를 활용하기 전에 DB를 업데이트 하는 메서드.
     */
    @Override
    @Transactional
    public List<WeatherMidCompositeKey> updateWeatherMid(List<WeatherMidEntity> entities) {
        /*
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
    /**
     * 중기 예보 조회 openAPI를 호출하여 데이터를 담아 ResultDTO<List<WeatherMidDTO>>를 리턴
     *
     * @param regionCode 지역코드
     * @return ResultDTO<List<WeatherMidDTO>>
     */
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

    //행정동 주소로 mid data 조회

    @Override
    /**
     * 지역 이름과 도시 이름을 매개변수로 받아 해당하는 위치의 2일 후부터 6일 후까지의 중기 예보를 ResultDTO<List<WeatherMidDTO>>로 리턴하는 메서드
     *
     * @param region1 지역 이름
     * @param region2 도시 이름
     * @return ResultDTO<List<WeatherMidDTO>>
     */
    public ResultDTO<List<WeatherMidDTO>> getMidForecastAddress(String region1, String region2) {
        String[] weeks = dateService.getDaysAfterToday(2, 6);
        List<WeatherMidDTO> dtoList = new ArrayList<>();
        region2 = region2.substring(0, region2.length()-1);
        log.info(region1 + region2);
        for (int i = 0; i < weeks.length; i++) {
            if(region1.equals("서울")){
                WeatherMidEntity result = weatherMidRepository.findByRegionNameAndIdBaseTime(region1,weeks[i]).orElseThrow(() -> new NoSuchElementException());
                dtoList.add(entityToDTO(result));
                log.info("서울");
            }
            else if(region1.equals("경기") && region2.equals("광주")){
                System.out.println(region2);
                WeatherMidEntity result = weatherMidRepository.findByRegionNameAndIdBaseTimeAndCity("광주", weeks[i], "경기남부").orElseThrow(() -> new NoSuchElementException());
                dtoList.add(entityToDTO(result));
                log.info("경기 광주");
            }
            else if(region1.equals("광주")){
                WeatherMidEntity result = weatherMidRepository.findByRegionNameAndIdBaseTimeAndCity("광주", weeks[i], "전남").orElseThrow(() -> new NoSuchElementException());
                dtoList.add(entityToDTO(result));
                log.info("광주");

            }
            else if(region2.equals("고성") && region1.equals("경남")){
                WeatherMidEntity result = weatherMidRepository.findByRegionNameAndIdBaseTimeAndCity(region2, weeks[i], "경남").orElseThrow(() -> new NoSuchElementException());
                dtoList.add(entityToDTO(result));
                log.info("경남 고성");

            }
            else if(region2.equals("고성") && region1.equals("강원")){
                WeatherMidEntity result = weatherMidRepository.findByRegionNameAndIdBaseTimeAndCity(region2, weeks[i], "강원영동").orElseThrow(() -> new NoSuchElementException());
                dtoList.add(entityToDTO(result));
                log.info("강원 고성");
            }else{
                WeatherMidEntity result = weatherMidRepository.findByRegionNameAndIdBaseTime(region2, weeks[i]).orElseThrow(() -> new NoSuchElementException());
                dtoList.add(entityToDTO(result));
                System.out.println(result);
                log.info("기본");

            }

        }
        return ResultDTO.of(HttpStatus.OK.value(), "날씨 중기 예보를 조회하는데 성공하였습니다.", dtoList);
    }





}
