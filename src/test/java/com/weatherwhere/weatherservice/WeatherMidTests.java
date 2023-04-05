package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
import com.weatherwhere.weatherservice.service.date.DateService;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;
import com.weatherwhere.weatherservice.service.weathermid.ParseCSVService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class WeatherMidTests {
    @Autowired
    private WeatherMidService weatherMidService;

    @Autowired
    private ParseCSVService parseCSVService;

    @Autowired
    private DateService dateService;

    @Test
    @DisplayName("중기 기온 예보와 중기 육상 예보 2개의 openAPI를 호출하는 테스트")
    void testWeatherOpenAPI() {
        try {
            System.out.println(weatherMidService.getWeatherMidTa("11111111", dateService.getTmfc()));
            System.out.println(weatherMidService.getWeatherMidLandFcst("11H20000", dateService.getTmfc()));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("중기 예보 API 2개를 합쳐 Entity 리스트를 반환하는 메서드를 확인.")
    void testGetWeatherMidEntities() {
        long start = System.nanoTime();
        List<WeatherMidEntity> entities = weatherMidService.makeEntityList(parseCSVService.ParseCSV(),
                dateService.getDaysAfterToday(3, 7), dateService.getTmfc());
        long openApiEnd = System.nanoTime();
        System.out.println("약 850번의 OpenAPI 호출을 통해 850개의 Entity리스트를 만드는데 소요되는시간: " + (openApiEnd - start) + "ns");


        List<WeatherMidCompositeKey> keys = weatherMidService.updateWeatherMid(entities);
        long dbEnd = System.nanoTime();
        System.out.println("약 850개의 Entity리스트를 DB에 업데이트하는데 소요되는 시간: " + (dbEnd - openApiEnd) + "ns");
        System.out.println(keys.toString());
    }
}
