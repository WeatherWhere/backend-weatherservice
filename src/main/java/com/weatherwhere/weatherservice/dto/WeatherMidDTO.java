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
    private Long mid_term_forecast_id;
    private String base_time;
    private Long tmn;
    private Long tmx;
    private String region_code;
    private Long r_am;
    private Long r_pm;
    private String w_am;
    private String w_pm;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
