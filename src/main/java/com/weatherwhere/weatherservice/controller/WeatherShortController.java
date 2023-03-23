package com.weatherwhere.weatherservice.controller;

//import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import com.weatherwhere.weatherservice.service.WeatherShortMainService;
import com.weatherwhere.weatherservice.service.WeatherShortMainServiceImpl;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
        import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherShortController {


    private final WeatherShortMainService weatherShortMainService;


    @GetMapping("/save-weather-short-main")
    public String weatherShortMainEntityList() throws IOException, URISyntaxException, ParseException {
        String nx = "69";
        String ny = "100";
        String baseDate = "20230322";
        String baseTime = "2000";
        return weatherShortMainService.getWeatherShortEntity(nx,ny,baseDate,baseTime);
    }



}
