package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.service.WeatherMidService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class WeatherserviceApplicationTests {
    @Autowired
    private WeatherMidService weatherMidService;

    @Test
    void testMidWeatherRegister() {
        WeatherMidDTO dto = WeatherMidDTO.builder()
                .base_time("20230323")
                .tmn(Long.parseLong("11"))
                .tmx(Long.parseLong("19"))
                .region_code("11B10101")
                .r_am(Long.parseLong("30"))
                .r_pm(Long.parseLong("40"))
                .w_am("구름많음")
                .w_pm("흐리고 비")
                .build();
        Long mid_term_forecast_id = weatherMidService.register(dto);
        System.out.println(mid_term_forecast_id);
    }

    @Test
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
    void testGetDaysAfterToday() {
        System.out.println(Arrays.toString(weatherMidService.getDaysAfterToday(3, 7)));
    }

    @Test
    @DisplayName("중기예보 API 2개를 합쳐 하나의 테이블을 업데이트합니다.")
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
