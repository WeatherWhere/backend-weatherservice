package com.weatherwhere.weatherservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather_mid_term", schema = "weather")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherMidEntity extends BaseEntity {
    // 중기 예보 식별자
    @EmbeddedId
    private WeatherMidCompositeKey id;

    // 일 최저기온
    @Column(name = "tmn")
    private Long tmn;

    // 일 최고기온
    @Column(name = "tmx")
    private Long tmx;

    // 오전 강수 확률
    @Column(name = "r_am")
    private Long rAm;

    // 오후 강수 확률
    @Column(name = "r_pm")
    private Long rPm;

    // 오전 날씨
    @Column(name = "w_am")
    private String wAm;

    // 오후 날씨
    @Column(name = "w_pm")
    private String wPm;
}
