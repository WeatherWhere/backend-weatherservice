package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherXYRepository;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherShortTests {

    //@ModelAttribute사용하여 dto값으로 request 받을 때 mockmvc패턴 사용하여 테스트
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherShortMainApiService weatherShortMainApiService;

    @Autowired
    private WeatherXYRepository weatherXYRepository;

    @Test
    @DisplayName("LocalDate형식으로 db에 단기예보 값 저장하기")
    void testLocaDate() throws Exception {
        String[] nxList = {"53", "54", "54", "54"};
        String[] nyList = {"125", "123", "126", "129"};
        String baseDate = "20230326";
        String baseTime = "0500";

        for (int i = 0; i < nxList.length; i++) {
            MvcResult result = mockMvc.perform(get("/weather/save-weather-short-main")
                            .param("nx", nxList[i])
                            .param("ny", nyList[i])
                            .param("baseDate", baseDate)
                            .param("baseTime", baseTime))

                    .andExpect(status().isOk())
                    .andReturn();
        }

    }

    @Test
    @DisplayName("nx,ny별 단기예보 데이터 불러오기 테스트")
    void testNxNyRepeat() throws Exception {
        String baseDate = "20230328";
        String baseTime = "0500";
        MvcResult result = mockMvc.perform(get("/weather/save-weather-short-main")
                        .param("baseDate", baseDate)
                        .param("baseTime", baseTime))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("위경도 nx, ny로 변경되는지 테스트")
    void testLocationToNxNy() {
        WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO = new WeatherShortMainApiRequestDTO();
        weatherShortMainApiRequestDTO.setLocationX(37.56356944444444);
        weatherShortMainApiRequestDTO.setLocationY(126.98000833333333);
        Assertions.assertEquals(weatherShortMainApiService.getGridXY(weatherShortMainApiRequestDTO).getNx(), 60);

    }

    @Test
    @DisplayName("nx, ny에 해당하는 데이터 30시간씩 추출하는 테스트")
    void testGetMainData() {
        WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO = new WeatherShortMainApiRequestDTO();
        weatherShortMainApiRequestDTO.setLocationX(37.489325);
        weatherShortMainApiRequestDTO.setLocationY(126.554234);
        weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO);
        System.out.println(weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO));
    }


    @Test
    @DisplayName("테이블에서 모든 nx, ny값 불러오기")
    void testNxNyList() {

        List<Object[]> xyList = weatherXYRepository.findAllNxAndNy();
        System.out.println("xy리스트:" + xyList);
        Integer count = 0;
        for (Object[] xy : xyList) {
            Integer nx = (Integer) xy[0];
            Integer ny = (Integer) xy[1];
            count++;
            System.out.println(nx+", "+ny + "/" + count);
        }

    }
}
