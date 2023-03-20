package com.weatherwhere.weatherservice.controller;

import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
//import com.weatherwhere.weatherservice.service.WeatherRestTemService;
import com.weatherwhere.weatherservice.service.WeatherShortMainServiceImpl;
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
public class WeatherShortController {


    private final WeatherShortMainServiceImpl weatherShortMainService;


    @GetMapping("/rest-tem")
    public List<WeatherShortMainDto> weatherShortMainDtoList() throws IOException, URISyntaxException, ParseException {

        return weatherShortMainService.getWeatherShortDto();
    }


}
