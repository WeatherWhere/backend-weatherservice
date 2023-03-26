package com.weatherwhere.weatherservice.dto;

import com.weatherwhere.weatherservice.domain.WeatherXY;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeatherShortAllDTO {

    //발표날짜
    private String baseDate;

    //발표시간
    private String baseTime;

    //예보날짜+예보시간
    private LocalDateTime fcstDateTime;

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

    private WeatherXY weatherXY;


}
