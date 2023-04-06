package com.weatherwhere.weatherservice.domain.weathershort;

import com.weatherwhere.weatherservice.domain.BaseEntity;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="weather_short_term_sub", schema = "weather")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeatherShortSub extends BaseEntity {

    // 단기 예보 식별자
    @EmbeddedId
    private WeatherShortCompositeKey id;

    //발표날짜
    @Column(name = "base_date")
    private String baseDate;

    //발표시간
    @Column(name = "base_time")
    private String baseTime;

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


}

