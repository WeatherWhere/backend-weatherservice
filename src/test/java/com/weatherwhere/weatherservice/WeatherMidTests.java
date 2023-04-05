package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
import com.weatherwhere.weatherservice.service.date.DateService;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;
import com.weatherwhere.weatherservice.service.weathermid.ParseCSVService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
            System.out.println(weatherMidService.getWeatherMidTa("11111111", "202304041800"));
            System.out.println(weatherMidService.getWeatherMidLandFcst("11H20000", "202304041800"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


//    @Test
//    @DisplayName("중기예보 API 2개를 합쳐 하나의 테이블을 업데이트하여 생성된 기본키를 리스트로 리턴")
//    void testUpdateWeatherMid() {
//        List<RegionCodeDTO> regionCodes = parseCSVService.ParseCSV();
//        String tmfc = "202304041800";
//
//        try {
//            for (int i = 0; i < regionCodes.size(); i++) {
//                System.out.println((weatherMidService.updateWeatherMid(regionCodes.get(i), tmfc)));
//            }
//        } catch (Exception e) {
//            System.out.println(e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    @Test
    @DisplayName("중기 예보 API 2개를 합쳐 Entity 리스트를 반환하는 메서드를 확인.")
    void testGetWeatherMidEntities() {
        List<WeatherMidEntity> entities = weatherMidService.makeEntityList(parseCSVService.ParseCSV(),
                dateService.getDaysAfterToday(3, 7), "202304051800");
        for (WeatherMidEntity entity: entities) {
            System.out.println(entity);
        }
    }
}
