package com.weatherwhere.weatherservice.service.date;

import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;

public interface DateService {
    String[] getDaysAfterToday(int start, int end);

    String getTmfc();

    WeatherShortRequestDTO getBaseDateTime(WeatherShortRequestDTO weatherShortRequestDTO);
}
