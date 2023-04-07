package com.weatherwhere.weatherservice.domain.weathershort;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class WeatherShortCompositeKey implements Serializable {
    //예보날짜+시간
    @Column(name = "fcst_date_time")
    private LocalDateTime fcstDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_xy_id")
    @MapsId("weatherXY")
    private WeatherXY weatherXY;

}
