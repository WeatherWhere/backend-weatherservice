package com.weatherwhere.weatherservice.dto;

import lombok.Data;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WeatherShortMainApiRequestDTO {

    //발표날짜
    private LocalDate fcstDate;

    //발표시간
    private LocalTime fcstTime;

    private Double locationX;

    private Double locationY;

    private Double nx;
    private Double ny;


}


