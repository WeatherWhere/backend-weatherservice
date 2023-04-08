package com.weatherwhere.weatherservice.dto.weathershort;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeatherShortSubDTO {

    //xy 외래키
    private Long xyId;

    private Long weatherShortId;

    private Long weatherXYId;

    //예보날짜+예보시간
    private LocalDateTime fcstDateTime;


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
