package com.weatherwhere.weatherservice.dto.tour;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShortMainDTO {
    //격자 x
    private Integer weatherX;

    //격자 y
    private Integer weatherY;

    //강수확률
    private Double pop;

    //강수형태
    private Double pty;

    //하늘상태
    private Double sky;

    //1시간단위 기온
    private Double tmp;

    //풍속
    private Double wsd;

    //습도
    private Double reh;

    /*
    //일 최저기온
    private Double tmn;

    //일 최고기온
    private Double tmx;

    // 예보 날짜
    private LocalDate fcstDate;*/
}
