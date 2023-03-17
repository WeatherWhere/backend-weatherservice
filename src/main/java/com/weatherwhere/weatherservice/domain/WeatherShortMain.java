package com.weatherwhere.weatherservice.domain;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name="weather_short_term_main", schema = "weather")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WeatherShortMain {

    //identity방식으로 아이디 1씩 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_short_id")
    private Long weather_short_id;

    //격자 x
    @Column(name = "weather_x")
    private Integer weather_x;

    //격자 y
    @Column(name = "weather_y")
    private Integer weather_y;


    //발표날짜
    @Column(name = "base_date")
    private String base_date;

    //발표시간
    @Column(name = "base_time")
    private String base_time;

    //예보날짜
    @Column(name = "fcst_date")
    private String fcst_date;

    //예보시간
    @Column(name = "fcst_time")
    private String fcst_time;

    //강수확률
    @Column(name = "pop")
    private Double pop;

    //강수형태
    @Column(name = "pty")
    private Double pty;

    @Column(name = "pcp")
    private Double pcp;

    //하늘상태
    @Column(name = "sky")
    private Double sky;

    //1시간단위 기온
    @Column(name = "tmp")
    private Double tmp;

    //일 최저기온
    @Column(name = "tmn")
    private Double tmn;

    //일 최고기온
    @Column(name = "tmx")
    private Double tmx;

    //풍속
    @Column(name = "wsd")
    private Double wsd;

    //습도
    @Column(name = "reh")
    private Double reh;


}
