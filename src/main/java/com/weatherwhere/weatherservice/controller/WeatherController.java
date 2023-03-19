package com.weatherwhere.weatherservice.controller;

import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
//import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherController {


    private final WeatherRestTemService weatherRestTemService;


    @GetMapping("/rest-tem")
    public List<WeatherShortMainDto> weatherShortMainDtoList() throws IOException, URISyntaxException, ParseException {

        return weatherRestTemService.getWeatherShortDto();
    }


}
