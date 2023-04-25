package com.weatherwhere.weatherservice.dto.weathershort;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WeatherShortMainApiRequestDTO {

    /**
     * 예보날짜+시간
     */
    private LocalDateTime fcstDateTime;

    /**
     * 위도
     */
    private Double locationX;

    /**
     * 경도
     */
    private Double locationY;

    /**
     * 격자 x
     */
    private Integer nx;

    /**
     * 격자 y
     */
    private Integer ny;


}


