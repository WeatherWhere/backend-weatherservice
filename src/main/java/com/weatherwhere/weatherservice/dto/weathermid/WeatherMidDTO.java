package com.weatherwhere.weatherservice.dto.weathermid;

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
/**
 * DB에 regionCode를 기준으로 중기 예보 데이터를 저장할 DTO
 */
public class WeatherMidDTO {
    private String regionCode;
    private String baseTime;
    private String regionName;
    private String city;
    private Long tmn;
    private Long tmx;
    private Long rAm;
    private Long rPm;
    private String wAm;
    private String wPm;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
