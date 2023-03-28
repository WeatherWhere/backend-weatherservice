package com.weatherwhere.weatherservice.domain.weathershort;


import com.weatherwhere.weatherservice.domain.BaseEntity;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name="weather_short_term_main", schema = "weather",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"fcst_date_time", "weather_xy_id"})})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "weatherXY")
public class WeatherShortMain extends BaseEntity {

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

    //강수확률
    @Column(name = "pop")
    private Double pop;

    //강수형태
    @Column(name = "pty")
    private Double pty;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_xy_id")
    private WeatherXY weatherXY;

    //테이블 값 업데이트
    public void update(WeatherShortAllDTO dto) {
        this.pop = dto.getPop();
        this.pty = dto.getPty();
        this.reh = dto.getReh();
        this.sky = dto.getSky();
        this.tmp = dto.getTmp();
        this.wsd = dto.getWsd();
        this.tmx = dto.getTmx();
        this.tmn = dto.getTmn();
        this.baseTime = dto.getBaseTime();
        this.baseDate = dto.getBaseDate();
    }



    //테이블 값 set
    public void setWeatherXY(WeatherXY weatherXY){
        this.weatherXY =weatherXY;
    }

}
