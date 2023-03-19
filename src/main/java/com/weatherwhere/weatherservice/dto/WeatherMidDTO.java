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
    private Double tmn;
    private Double tmx;
    private String region_code;
    private Double r_am;
    private Double r_pm;
    private String w_am;
    private String w_pm;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
