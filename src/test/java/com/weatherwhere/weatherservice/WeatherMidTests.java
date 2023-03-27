package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.domain.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.dto.parse.RegionCodeDTO;
import com.weatherwhere.weatherservice.service.WeatherMidService;
import com.weatherwhere.weatherservice.service.parse.ParseCSVService;
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
    @DisplayName("중기 예보 DTO를 만들어 DB에 저장되는지 확인하는 테스트")
    void testMidWeatherRegister() {
        WeatherMidDTO dto = WeatherMidDTO.builder()
                .regionCode("11B10101")
                .baseTime("20230323")
                .tmn(Long.parseLong("11"))
                .tmx(Long.parseLong("19"))
                .rAm(Long.parseLong("30"))
                .rPm(Long.parseLong("40"))
                .wAm("구름많음")
                .wPm("흐리고 비")
                .build();
        WeatherMidCompositeKey id = weatherMidService.register(dto);
        System.out.println(id);
    }

    @Test
    @DisplayName("중기 기온 예보와 중기 육상 예보 2개의 openAPI를 호출하는 테스트")
    void testWeatherOpenAPI() {
        try {
            System.out.println(weatherMidService.getWeatherMidTa("11B10101", "202303220600"));
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
