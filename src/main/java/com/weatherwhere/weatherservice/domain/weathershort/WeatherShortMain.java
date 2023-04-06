package com.weatherwhere.weatherservice.domain.weathershort;


import com.weatherwhere.weatherservice.domain.BaseEntity;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name="weather_short_term_main", schema = "weather")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeatherShortMain extends BaseEntity {

    // 단기 예보 식별자
    @EmbeddedId
    private WeatherShortCompositeKey id;

    //발표날짜
    @Column(name = "base_date")
    private String baseDate;

    //발표시간
    @Column(name = "base_time")
    private String baseTime;

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



}
