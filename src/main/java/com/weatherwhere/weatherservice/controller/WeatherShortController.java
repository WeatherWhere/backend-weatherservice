package com.weatherwhere.weatherservice.controller;

//import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import com.weatherwhere.weatherservice.dto.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherShortController {


    private final WeatherShortMainService weatherShortMainService;


    @GetMapping("/save-weather-short-main")
    public String weatherShortMainEntityList(@RequestBody WeatherShortRequestDTO weatherShortRequestDTO) throws IOException, URISyntaxException, ParseException {
/*        String nx = "69";
        String ny = "100";
        String baseDate = "20230323";
        String baseTime = "1700";*/
        return weatherShortMainService.getWeatherShortEntity(weatherShortRequestDTO);
    }



}
