package com.weatherwhere.weatherservice.service.tour;

import java.util.List;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;

public interface TourApiService {
    ResultDTO<List<WeatherMidDTO>> getWeatherMidData(String baseTime);
}
