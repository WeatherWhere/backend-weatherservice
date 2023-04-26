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

    /**
     * 위치를 기반으로 날씨 중기예보(3일 후 - 7일 후) 데이터를 리턴합니다.
     *
     * @param region1 DB 조회를 위한 '지역이름'
     * @param region2 DB 조회를 위한 '도시이름'
     * @return 성공 시 ResultDTO<List<WeatherMidDTO>>에 statusCode=200, message, data를 리턴, 실패 시 statusCode 404와 message 리턴
     */
    @GetMapping("/forecast/mid")
    public ResultDTO<List<WeatherMidDTO>> getWeatherMidForecastAddress(@RequestParam("region1") String region1, @RequestParam("region2") String region2) {
        ResultDTO<List<WeatherMidDTO>> data = weatherMidService.getMidForecastAddress(region1, region2);
        return data;
    }


}
