package com.weatherwhere.weatherservice.controller.weathermid;

import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.exception.ErrorCode;
import com.weatherwhere.weatherservice.exception.RestApiException;
import com.weatherwhere.weatherservice.service.tour.TourApiService;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherMidController {
    private final WeatherMidService weatherMidService;
    private final TourApiService tourApiService;
    @GetMapping("/forecast/week")
    public ResultDTO<List<WeatherMidDTO>> getWeatherMidForecast(@RequestParam("regionCode") String regionCode) {
        if (regionCode.length() != 8) {
            throw new RestApiException(ErrorCode.BAD_REQUEST, "지역 코드는 8글자이어야 합니다.");
        }
        ResultDTO<List<WeatherMidDTO>> data = weatherMidService.getMidForecast(regionCode);
        return data;
    }

    @GetMapping(value = "/tour/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDTO<List<WeatherMidDTO>> getWeatherMidData(@RequestParam("baseDate") String baseDate) {
        ResultDTO<List<WeatherMidDTO>> list = tourApiService.getWeatherMidData(baseDate);
        return list;
    }
}
