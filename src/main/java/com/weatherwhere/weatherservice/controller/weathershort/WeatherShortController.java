package com.weatherwhere.weatherservice.controller.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;

import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherShortController {

    private final WeatherShortMainService weatherShortMainService;
    private final WeatherShortMainApiService weatherShortMainApiService;

/*
    //nx, ny별 단기예보 데이터 저장하는 api
    @GetMapping("/forecast/short")
    public String weatherShortMainEntityList(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {
        try {

            List<WeatherShortMain> mainEntityList = Collections.synchronizedList(new ArrayList<>());
            List<WeatherShortSub> subEntityList = Collections.synchronizedList(new ArrayList<>());

            return weatherShortMainService.getXYListWeatherAllSave(weatherShortRequestDTO, mainEntityList, subEntityList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);

        }
    }*/

    //단기예보 메인 api
    @GetMapping("/forecast/short/main")
    public ResultDTO<Object> getWeatherShortMainResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO);
    }

    //단기예보 메인(현재시간) api
    @GetMapping("/forecast/short/main/now")
    public ResultDTO<Object> getWeatherShortMainNowResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
         return weatherShortMainApiService.getWeatherShortMainNowData(weatherShortMainApiRequestDTO);
    }



    //단기예보 서브 api
    @GetMapping("/forecast/short/sub")
    public ResultDTO<Object> getWeatherShortSubResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortSubData(weatherShortMainApiRequestDTO);
    }


}
