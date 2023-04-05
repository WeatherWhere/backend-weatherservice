package com.weatherwhere.weatherservice.controller.weathershort;

import com.weatherwhere.weatherservice.dto.ResultDto;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortSubDTO;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;

import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherShortController {

    private final WeatherShortMainService weatherShortMainService;
    private final WeatherShortMainApiService weatherShortMainApiService;


    //nx, ny별 단기예보 데이터 저장하는 api
    @GetMapping("/forecast/short")
    public String weatherShortMainEntityList(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {
        try {
            return weatherShortMainService.saveWeatherShortEntity(weatherShortRequestDTO);

        } catch (Exception e) {
            throw new Exception(e);

        }
    }

    //단기예보 메인 api
    @GetMapping("/forecast/short/main")
    public ResultDto<Object> getWeatherShortMainResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO);
    }

    //단기예보 메인(현재시간) api
    @GetMapping("/forecast/short/main/now")
    public ResultDto<Object> getWeatherShortMainNowResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
         return weatherShortMainApiService.getWeatherShortMainNowData(weatherShortMainApiRequestDTO);
    }



    //단기예보 서브 api
    @GetMapping("/forecast/short/sub")
    public ResultDto<Object> getWeatherShortSubResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortSubData(weatherShortMainApiRequestDTO);
    }


}
