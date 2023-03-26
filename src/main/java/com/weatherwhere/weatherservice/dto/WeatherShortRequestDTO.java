package com.weatherwhere.weatherservice.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class WeatherShortRequestDTO {
    //발표날짜
    private String baseDate;

    //발표시간
    private String baseTime;

    private Integer nx;
    private Integer ny;
}
