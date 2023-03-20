package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.service.WeatherMidService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherserviceApplicationTests {
    @Autowired
    private WeatherMidService weatherMidService;
    @Test
    void testMidWeatherRegister() {
        WeatherMidDTO dto = WeatherMidDTO.builder()
                .base_time("20230323")
                .tmn(Double.parseDouble("11"))
                .tmx(Double.parseDouble("19"))
                .region_code("11B10101")
                .r_am(Double.parseDouble("30"))
                .r_pm(Double.parseDouble("40"))
                .w_am("구름많음")
                .w_pm("흐리고 비")
                .build();
        Long mid_term_forecast_id = weatherMidService.register(dto);
        System.out.println(mid_term_forecast_id);
    }

}
