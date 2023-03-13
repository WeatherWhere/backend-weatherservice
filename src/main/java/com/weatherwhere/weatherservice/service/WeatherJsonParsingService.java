package com.weatherwhere.weatherservice.service;


import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
import com.weatherwhere.weatherservice.repository.WeatherShortMainRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
@Transactional
public class WeatherJsonParsingService {

    private final WeatherShortMainRepository weatherShortMainRepository;

    enum WeatherValue {
        POP, PTY, SKY, TMP, TMN, TMX, WSD, REH
    }

    public WeatherShortMainDto WeatherJsonParsing() throws Exception {

        // 입력받은 값을 넣어둘 DTO
        WeatherShortMainDto weatherShortMainDto = new WeatherShortMainDto();

        // 변수 설정
        String apiURL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String authKey = System.getenv("WEATHER_SHORT_SERVICE_KEY"); // 본인 서비스 키 입력

        String nx = "69";
        String ny = "100";
        String baseDate = "20230312";
        String baseTime = "0500";
        String dataType = "JSON";

        StringBuilder urlBuilder = new StringBuilder(apiURL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + authKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows=153", "UTF-8"));    // 숫자 표
        urlBuilder.append("&" + URLEncoder.encode("pageNo=1", "UTF-8"));    // 페이지 수
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); // 받으려는 타입
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //격자x
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //격자y

        URL url = new URL(urlBuilder.toString());
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();

        System.out.println(result);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        JSONObject parse_response = (JSONObject) jsonObject.get("response");
        JSONObject parse_body = (JSONObject) parse_response.get("body"); // response 로 부터 body 찾아오기
        JSONObject parse_items = (JSONObject) parse_body.get("items"); // body 로 부터 items 받아오기
        // items 로 부터 itemList : 뒤에 [ 로 시작하므로 jsonArray 이다.
        JSONArray parse_item = (JSONArray) parse_items.get("item");
        System.out.println("--------------------------");

        // item 들을 담은 List 를 반복자 안에서 사용하기 위해 미리 명시
        JSONObject object;
        // item 내부의 category 를 보고 사용하기 위해서 사용
        String category;
        Float value;

        // jsonArray를 반복자로 반복
        for (int temp = 0; temp < parse_item.size(); temp++) {
            object = (JSONObject) parse_item.get(temp);
            category = (String) object.get("category"); // item 에서 카테고리를 검색

            // Error 발생할수도 있으며 받아온 정보를 double이 아니라 문자열로 읽으면 오류
            value = Float.parseFloat((String) object.get("fcstValue"));

            WeatherValue weatherValue = WeatherValue.valueOf(category);


            switch (weatherValue) {
                case POP:
                    weatherShortMainDto.setPop(value);
                    break;
                case PTY:
                    weatherShortMainDto.setPty(value);
                    break;
                case SKY:
                    weatherShortMainDto.setSky(value);
                    break;
                case TMP:
                    weatherShortMainDto.setTmp(value);
                    break;
                case TMN:
                    weatherShortMainDto.setTmn(value);
                    break;
                case TMX:
                    weatherShortMainDto.setTmx(value);
                    break;
                case WSD:
                    weatherShortMainDto.setWsd(value);
                    break;
                case REH:
                    weatherShortMainDto.setReh(value);
                    break;
                default:
                    break;
            }
        }
        weatherShortMainDto.setBase_date(baseDate);
        weatherShortMainDto.setBase_time(baseTime);
        weatherShortMainDto.setWeather_x(Integer.parseInt(nx));
        weatherShortMainDto.setWeather_y(Integer.parseInt(ny));
        System.out.println(weatherShortMainDto);

        return weatherShortMainDto;
    }
}
