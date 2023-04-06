package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortMainRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherXYRepository;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
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

    @Autowired
    private WeatherShortMainRepository weatherShortMainRepository;


    @Test
    @DisplayName("nx,ny별 단기예보 데이터 불러오기 테스트")
    @Disabled
    void testNxNyRepeat() throws Exception {
        String baseDate = "20230406";
        String baseTime = "2000";
        MvcResult result = mockMvc.perform(get("/weather/forecast/short")
                        .param("baseDate", baseDate)
                        .param("baseTime", baseTime))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    @DisplayName("단기에보 메인 api 테스트")
    void testGetMainData() throws Exception {
        WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO = new WeatherShortMainApiRequestDTO();
        weatherShortMainApiRequestDTO.setLocationX(37.489325);
        weatherShortMainApiRequestDTO.setLocationY(126.554234);
        weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO);
        System.out.println(weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO));
    }

    @Test
    @DisplayName("단기에보 메인 api(현재 시간) 테스트")
    void testGetMainDataNow() throws Exception {
        WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO = new WeatherShortMainApiRequestDTO();
        weatherShortMainApiRequestDTO.setLocationX(37.489325);
        weatherShortMainApiRequestDTO.setLocationY(126.554234);
        System.out.println(weatherShortMainApiService.getWeatherShortMainNowData(weatherShortMainApiRequestDTO));
    }


    @Test
    @DisplayName("단기에보 서브 api 테스트")
    void testGetSubData() throws Exception {
        WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO = new WeatherShortMainApiRequestDTO();
        weatherShortMainApiRequestDTO.setLocationX(37.489325);
        weatherShortMainApiRequestDTO.setLocationY(126.554234);
        System.out.println(weatherShortMainApiService.getWeatherShortSubData(weatherShortMainApiRequestDTO));
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
