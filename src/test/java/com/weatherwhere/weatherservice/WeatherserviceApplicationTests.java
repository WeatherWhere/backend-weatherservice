package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class WeatherserviceApplicationTests {
    @Autowired
    private WeatherShortMainService weatherShortMainService;

    //weathercsv에 있는 격자 x,y값 db에 저장
    @Test
    @Disabled
    void testWeatherXYRead() throws IOException {
        weatherShortMainService.readWeatherXYLocation();
        System.out.println(weatherShortMainService.readWeatherXYLocation());
    }

    @Test
    @DisplayName("격자 x,y 값 별 단기예보 받아오기")
    void testWeatherShortXY() throws IOException{

    }
}
