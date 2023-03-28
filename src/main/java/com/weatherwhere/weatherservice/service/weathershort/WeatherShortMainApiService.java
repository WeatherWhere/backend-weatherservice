package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;

import java.util.List;

public interface WeatherShortMainApiService {
    WeatherShortMainApiRequestDTO getGridXY(WeatherShortMainApiRequestDTO requestDTO);
    List<WeatherShortMainDTO> getWeatherShortMainData(WeatherShortMainApiRequestDTO requestDTO);
    default WeatherShortMainDTO entityToDTO(WeatherShortMain entity) {
        WeatherShortMainDTO dto = WeatherShortMainDTO.builder()
                .weatherShortId(entity.getWeatherShortId())
                .weatherXYId(entity.getWeatherXY().getId())
                .fcstDateTime(entity.getFcstDateTime())
                .pop(entity.getPop())
                .pty(entity.getPty())
                .reh(entity.getReh())
                .sky(entity.getSky())
                .tmp(entity.getTmp())
                .wsd(entity.getWsd())
                .tmn(entity.getTmn())
                .tmx(entity.getTmx())
                .build();
        return dto;
    }


}
