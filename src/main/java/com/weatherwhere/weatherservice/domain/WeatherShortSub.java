package com.weatherwhere.weatherservice.domain;

import com.weatherwhere.weatherservice.dto.WeatherShortAllDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="weather_short_term_sub", schema = "weather",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"fcst_date_time", "weather_xy_id"})})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "weatherXY")
public class WeatherShortSub extends BaseEntity{

    //identity방식으로 아이디 1씩 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_short_id")
    private Long weatherShortId;

    //발표날짜
    @Column(name = "base_date")
    private String baseDate;

    //발표시간
    @Column(name = "base_time")
    private String baseTime;

    //예보날짜+시간
    @Column(name = "fcst_date_time")
    private LocalDateTime fcstDateTime;

    //1시간 강수량
    @Column(name = "pcp")
    private String pcp;

    //1시간 신적설
    @Column(name = "sno")
    private String sno;

    //풍속(동서성분)
    @Column(name = "uuu")
    private Double uuu;

    //풍속(남북성분)
    @Column(name = "vvv")
    private Double vvv;

    //파고
    @Column(name = "wav")
    private Double wav;

    //풍량
    @Column(name = "vec")
    private Double vec;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_xy_id")
    private WeatherXY weatherXY;

    public void update(WeatherShortAllDTO dto) {
        this.pcp = dto.getPcp();
        this.sno = dto.getSno();
        this.uuu = dto.getUuu();
        this.vvv = dto.getVvv();
        this.wav = dto.getWav();
        this.vec = dto.getVec();
        this.baseTime = dto.getBaseTime();
        this.baseDate = dto.getBaseDate();
    }

    public void setWeatherXY(WeatherXY weatherXY){
        this.weatherXY =weatherXY;
    }


}

