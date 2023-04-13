package com.weatherwhere.weatherservice.service.tour;

import java.util.List;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.tour.RankWeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;

public interface TourApiService {
    ResultDTO<List<RankWeatherShortMainDTO>> getRankWeatherShortMainData();
    default WeatherShortMainDTO entityToDTO(WeatherShortMain entity) {
        return WeatherShortMainDTO.builder()
            .sky(entity.getSky())
            .tmn(entity.getTmn())
            .tmp(entity.getTmp())
            .tmx(entity.getTmx())
            .wsd(entity.getWsd())
            .pop(entity.getPop())
            .pty(entity.getPty())
            .reh(entity.getReh())
            .fcstDateTime(entity.getId().getFcstDateTime())
            .build();
    }
}
