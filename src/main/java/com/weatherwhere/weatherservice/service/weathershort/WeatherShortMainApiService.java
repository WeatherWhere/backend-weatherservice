package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortSubDTO;

import java.util.List;

public interface WeatherShortMainApiService {
    WeatherShortMainApiRequestDTO getGridXY(WeatherShortMainApiRequestDTO requestDTO);
    //단기예보 메인 데이터 반환하는 서비스
    List<WeatherShortMainDTO> getWeatherShortMainData(WeatherShortMainApiRequestDTO requestDTO);

    //단기예보 서브 데이터 반환하는 서비스
    List<WeatherShortSubDTO> getWeatherShortSubData(WeatherShortMainApiRequestDTO requestDTO);

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

    default WeatherShortSubDTO subEntityToDTO(WeatherShortSub entity) {
        WeatherShortSubDTO dto = WeatherShortSubDTO.builder()
                .weatherShortId(entity.getWeatherShortId())
                .weatherXYId(entity.getWeatherXY().getId())
                .fcstDateTime(entity.getFcstDateTime())
                .pcp(entity.getPcp())
                .sno(entity.getSno())
                .uuu(entity.getUuu())
                .vvv(entity.getVvv())
                .wav(entity.getWav())
                .vec(entity.getVec())
                .build();
        return dto;
    }

}
