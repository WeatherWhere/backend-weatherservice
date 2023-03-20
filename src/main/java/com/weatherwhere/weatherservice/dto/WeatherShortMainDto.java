package com.weatherwhere.weatherservice.dto;


import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeatherShortMainDto {

    //격자 x
    private Integer weatherX;

    //격자 y
    private Integer weatherY;

    //발표날짜
    private String baseDate;

    //발표시간
    private String baseTime;

    //예보날짜
    private String fcstDate;

    //예보시간
    private String fcstTime;

    //강수확률
    private Double pop;

    //강수형태
    private Double pty;

    //하늘상태
    private Double sky;

    //강수량
    private Double pcp;

    //1시간단위 기온
    private Double tmp;

    //풍속
    private Double wsd;

    //습도
    private Double reh;

    //최저 최고는 중기예보에서 받아와서 사용
    //일 최저기온
    //private Double tmn;

    //일 최고기온
    //private Double tmx;


    /* DTO -> Entity */
    public WeatherShortMain toEntity() {
        WeatherShortMain weatherShortMain = WeatherShortMain.builder()
                .weatherX(weatherX)
                .weatherY(weatherY)
                .baseDate(baseDate)
                .baseTime(baseTime)
                .fcstDate(fcstDate)
                .fcstTime(fcstTime)
                .pop(pop)
                .pcp(pcp)
                .pty(pty)
                .reh(reh)
                .sky(sky)
                .tmp(tmp)
                .wsd(wsd)
                .build();
        return weatherShortMain;
    }


}
