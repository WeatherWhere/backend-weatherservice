package com.weatherwhere.weatherservice.dto;

import lombok.Getter;

@Getter

public class WeatherShortRequestDTO {
    //발표날짜
    private String baseDate;

    //발표시간
    private String baseTime;

    private String nx;
    private String ny;
}
