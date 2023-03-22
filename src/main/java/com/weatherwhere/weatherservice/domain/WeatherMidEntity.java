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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mid_term_forecast_id")
    private Long mid_term_forecast_id;

    // 예보 날짜
    @Column(name = "base_date")
    private String base_time;

    // 일 최저기온
    @Column(name = "tmn")
    private Long tmn;

    // 일 최고기온
    @Column(name = "tmx")
    private Long tmx;

    // 예보 구역 코드
    @Column(name = "region_code")
    private String region_code;

    // 오전 강수 확률
    @Column(name = "r_am")
    private Long r_am;

    // 오후 강수 확률
    @Column(name = "r_pm")
    private Long r_pm;

    // 오전 날씨
    @Column(name = "w_am")
    private String w_am;

    // 오후 날씨
    @Column(name = "w_pm")
    private String w_pm;
}
