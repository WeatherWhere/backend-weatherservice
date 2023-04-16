package com.weatherwhere.weatherservice.controller.weathermid;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.exception.ErrorCode;
import com.weatherwhere.weatherservice.exception.RestApiException;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherMidController {
    private final WeatherMidService weatherMidService;

    @GetMapping("/forecast/week")
    public ResultDTO<List<WeatherMidDTO>> getWeatherMidForecast(@RequestParam("regionCode") String regionCode) {
        if (regionCode.length() != 8) {
            throw new RestApiException(ErrorCode.BAD_REQUEST, "지역 코드는 8글자이어야 합니다.");
        }
        ResultDTO<List<WeatherMidDTO>> data = weatherMidService.getMidForecast(regionCode);
        return data;
    }

}
