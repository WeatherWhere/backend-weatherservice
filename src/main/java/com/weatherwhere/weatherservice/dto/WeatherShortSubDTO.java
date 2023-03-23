package com.weatherwhere.weatherservice.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeatherShortSubDTO {

    //발표날짜
    private String baseDate;

    //발표시간
    private String baseTime;
    //예보날짜
    private String fcstDate;

    //예보시간
    private String fcstTime;

    //1시간 강수량
    private String pcp;

    //1시간 신적설
    private String sno;

    //풍속(동서성분)
    private Double uuu;

    //풍속(남북성분)
    private Double vvv;

    //파고
    private Double wav;

    //풍량
    private Double vec;

}
