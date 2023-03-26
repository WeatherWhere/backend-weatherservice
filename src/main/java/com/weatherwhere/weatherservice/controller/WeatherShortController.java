package com.weatherwhere.weatherservice.controller;

//import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import com.weatherwhere.weatherservice.dto.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
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


    //getmapping에는 requestbody 사용 x
    @GetMapping("/save-weather-short-main")
    public String weatherShortMainEntityList(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws IOException, URISyntaxException, ParseException {
/*        String nx = "69";
        String ny = "100";
        String baseDate = "20230323";
        String baseTime = "1700";*/

        return weatherShortMainService.getWeatherShortEntity(weatherShortRequestDTO);
    }

    //위경도 받아서 격자 x,y로 변환한 뒤 해당 x,y에 대한 단기예보 데이터 전송하는 api
    @GetMapping("/weather-short-main-api")
    public WeatherShortMainApiRequestDTO getWeatherShortMainResponse (@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO){
        return weatherShortMainApiService.getGridXY(weatherShortMainApiRequestDTO);
    }






}
