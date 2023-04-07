package com.weatherwhere.weatherservice.service.tour;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;

public interface TourApiService {
    ResultDTO<WeatherMidDTO> getWeatherMidData(String regionCode, String baseDate);
}
