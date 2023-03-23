package com.weatherwhere.weatherservice.dto;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeatherShortMainDTO {

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

    //1시간단위 기온
    private Double tmp;

    //풍속
    private Double wsd;

    //습도
    private Double reh;

    //일 최저기온
    private Double tmn;

    //일 최고기온
    private Double tmx;


}
