package com.weatherwhere.weatherservice.controller;

import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
import com.weatherwhere.weatherservice.service.WeatherJsonParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherJsonParsingService weatherJsonParsingService;

    @GetMapping("/short-test")
    public WeatherShortMainDto weathertest() throws Exception{
        System.out.println(weatherJsonParsingService.WeatherJsonParsing());
        return weatherJsonParsingService.WeatherJsonParsing();
    }

    @GetMapping("/test")
    public String test(){
        return "테스트";
    }



}
