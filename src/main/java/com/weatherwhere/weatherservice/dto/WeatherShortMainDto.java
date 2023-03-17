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
    private Integer weather_x;

    //격자 y
    private Integer weather_y;

    //발표날짜
    private String base_date;

    //발표시간
    private String base_time;

    //예보날짜
    private String fcst_date;

    //예보시간
    private String fcst_time;

    //강수확률
    private Double pop;

    //강수형태
    private Double pty;

    //하늘상태
    private Double sky;

    private Double pcp;

    //1시간단위 기온
    //private Double tmp;

    //일 최저기온
    //private Double tmn;

    //일 최고기온
    //private Double tmx;

    //풍속
    private Double wsd;

    //습도
    private Double reh;



    /* DTO -> Entity */
    public WeatherShortMain toEntity() {
        WeatherShortMain weatherShortMain = WeatherShortMain.builder()
                .weather_x(weather_x)
                .weather_y(weather_y)
                .base_date(base_date)
                .base_time(base_time)
                .fcst_date(fcst_date)
                .fcst_time(fcst_time)
                .pop(pop)
                .pcp(pcp)
                .pty(pty)
                .reh(reh)
                .sky(sky)
                //.tmn(tmn)
                //.tmp(tmp)
                //.tmx(tmx)
                .wsd(wsd)
                .build();
        return weatherShortMain;
    }

}
