package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.dto.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDTO;

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
