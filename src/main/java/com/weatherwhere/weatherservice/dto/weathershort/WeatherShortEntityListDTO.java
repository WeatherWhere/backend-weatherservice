package com.weatherwhere.weatherservice.dto.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherShortEntityListDTO {
    /**
     * 단기예보 메인데이터 리스트
     */
    List<WeatherShortMain> mainEntityList;

    /**
     * 단기예보 서브데이터 리스트
     */
    List<WeatherShortSub> subEntityList;

}
