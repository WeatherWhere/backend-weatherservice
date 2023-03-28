package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
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

    @Test
    @DisplayName("중기 기온 예보와 중기 육상 예보 2개의 openAPI를 호출하는 테스트")
    void testWeatherOpenAPI() {
        try {
            System.out.println(weatherMidService.getWeatherMidTa("11111111", "202303220600"));
            System.out.println(weatherMidService.getWeatherMidLandFcst("11H20000", "202303220600"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("중기예보 API 2개를 합쳐 하나의 테이블을 업데이트하여 생성된 기본키를 리스트로 리턴")
    void testUpdateWeatherMid() {
        List<RegionCodeDTO> regionCodes = parseCSVService.ParseCSV();
        String tmfc = "202303270600";

        try {
            for (int i = 0; i < regionCodes.size(); i++) {
                System.out.println((weatherMidService.updateWeatherMid(regionCodes.get(i).getRegionCode(), tmfc)));
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}
