package com.weatherwhere.weatherservice.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WeatherShortMainApiRequestDTO {

    //예보시간+날짜
    private LocalDateTime fcstDateTime;

    private Double locationX;

    private Double locationY;

    private Integer nx;
    private Integer ny;


}


