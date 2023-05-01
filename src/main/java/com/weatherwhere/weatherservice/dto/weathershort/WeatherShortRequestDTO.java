package com.weatherwhere.weatherservice.dto.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Data
public class WeatherShortRequestDTO {
    //발표날짜
    private String baseDate;

    //어제 발표날짜
    private String beforeBaseDate;

    //발표시간
    private String baseTime;

    private WeatherXY weatherXY;

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
