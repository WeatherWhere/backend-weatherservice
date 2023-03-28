package com.weatherwhere.weatherservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherMidDTO {
    private String regionCode;
    private String baseTime;
    private Long tmn;
    private Long tmx;
    private Long rAm;
    private Long rPm;
    private String wAm;
    private String wPm;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
