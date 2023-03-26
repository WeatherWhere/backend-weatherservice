package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.dto.WeatherShortMainApiRequestDTO;

public interface WeatherShortMainApiService {

    WeatherShortMainApiRequestDTO getGridXY(WeatherShortMainApiRequestDTO requestDTO);

}
