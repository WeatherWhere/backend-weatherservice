package com.weatherwhere.weatherservice.dto.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherShortEntityListDTO {
    List<WeatherShortMain> mainEntityList;
    List<WeatherShortSub> subEntityList;

}
