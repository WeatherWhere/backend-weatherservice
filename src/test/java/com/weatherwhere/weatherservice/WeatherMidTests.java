package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.service.WeatherMidService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeatherMidTests {
    @Autowired
    private WeatherMidService weatherMidService;

    @Test
    @DisplayName("중기 예보 DTO를 만들어 DB에 저장되는지 확인하는 테스트")
    void testMidWeatherRegister() {
        WeatherMidDTO dto = WeatherMidDTO.builder()
                .baseTime("20230323")
                .tmn(Long.parseLong("11"))
                .tmx(Long.parseLong("19"))
                .regionCode("11B10101")
                .rAm(Long.parseLong("30"))
                .rPm(Long.parseLong("40"))
                .wAm("구름많음")
                .wPm("흐리고 비")
                .build();
        Long mid_term_forecast_id = weatherMidService.register(dto);
        System.out.println(mid_term_forecast_id);
    }

    @Test
    @DisplayName("중기 기온 예보와 중기 육상 예보 2개의 openAPI를 호출하는 테스트")
    void testWeatherOpenAPI() {
        try {
            System.out.println(weatherMidService.getWeatherMidTa("1.10E+102", "202303220600"));
            System.out.println(weatherMidService.getWeatherMidLandFcst("11H20000", "202303220600"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("중기예보 API 2개를 합쳐 하나의 테이블을 업데이트하여 생성된 기본키를 리스트로 리턴")
    void testUpdateWeatherMid() {
        String[] regIds = {"11B10101", "11D20501", "11H20201", "11F10201", "11F10203"};
        String tmfc = "202303220600";

        try {
            for (int i = 0; i < regIds.length; i++) {
                System.out.println((weatherMidService.updateWeatherMid(regIds[i], tmfc)));
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}
