package com.weatherwhere.weatherservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherShortTests {

    //@ModelAttribute사용하여 dto값으로 request 받을 때 mockmvc패턴 사용하여 테스트
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("LocalDate형식으로 db에 단기예보 값 저장하기")
    void testLocaDate() throws Exception {
        String[] nxList = {"53","54","54","54"};
        String[] nyList = {"125","123","126","129"};
        String baseDate = "20230326";
        String baseTime = "0500";

        for(int i=0; i< nxList.length; i++){
            MvcResult result = mockMvc.perform(get("/weather/save-weather-short-main")
                    .param("nx",nxList[i])
                    .param("ny",nyList[i])
                    .param("baseDate",baseDate)
                    .param("baseTime",baseTime))
                    .andExpect(status().isOk())
                    .andReturn();
        }

    }


}
