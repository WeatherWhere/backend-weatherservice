package com.weatherwhere.weatherservice.controller.weathermid;

import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherMidController {
    private final WeatherMidService weatherMidService;

    @GetMapping("/forecast/week")
    public ResponseEntity<List<WeatherMidDTO>> getWeatherMidForecast(@RequestParam("regionCode") String regionCode) {
        List<WeatherMidDTO> data = weatherMidService.getMidForecast(regionCode);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
