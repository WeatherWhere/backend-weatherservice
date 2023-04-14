package com.weatherwhere.weatherservice.dto.tour;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RankWeatherShortMainDTO {
    private String level1;
    private String level2;

    //격자 x
    private Integer weatherX;

    //격자 y
    private Integer weatherY;

    //예보날짜
    private LocalDate fcstDate;

    // 6 ~ 18시 누적 강수량
    private Double sumPcp;

    // 6 ~ 18시 하늘상태 평균
    private Double avgSky;

    // 일평균기온
    private Double avgTmp;

    // 6 ~ 18시 풍속 평균
    private Double avgWsd;

    // 일평균 습도
    private Double avgReh;

    // 최고 습도
    private Double maxReh;

    //일 최고기온
    private Double maxTmp;
}
