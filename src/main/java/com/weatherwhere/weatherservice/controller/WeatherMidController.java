package com.weatherwhere.weatherservice.controller;

import com.weatherwhere.weatherservice.domain.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.service.WeatherMidService;
import com.weatherwhere.weatherservice.service.date.DateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherMidController {
    private final WeatherMidService weatherMidService;
    @GetMapping("/forecast/week")
    public ResponseEntity<List<WeatherMidDTO>> getWeatherMidForecast (@RequestParam("regionCode") String regionCode) {
        try {
            List<WeatherMidDTO> data = weatherMidService.getMidForecast(regionCode);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
