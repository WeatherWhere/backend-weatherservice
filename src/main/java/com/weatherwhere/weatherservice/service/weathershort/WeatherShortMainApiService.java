package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortSubDTO;

public interface WeatherShortMainApiService {
    //단기예보 메인 데이터 반환하는 서비스
    ResultDTO<Object> getWeatherShortMainData(WeatherShortMainApiRequestDTO requestDTO) throws Exception;

    //단기예보 메인 데이터(현재 시간만) 반환하는 서비스
    ResultDTO<Object> getWeatherShortMainNowData(WeatherShortMainApiRequestDTO requestDTO) throws Exception;

    //단기예보 서브 데이터 반환하는 서비스
    ResultDTO<Object> getWeatherShortSubData(WeatherShortMainApiRequestDTO requestDTO) throws Exception;

    default WeatherShortMainDTO nowEntityToDTO(WeatherShortMain entity, Double tmn, Double tmx, Double beforeTmx) {
        WeatherShortMainDTO dto = WeatherShortMainDTO.builder()
                .xyId(entity.getId().getWeatherXY().getId())
                .fcstDateTime(entity.getId().getFcstDateTime())
                .pop(entity.getPop())
                .pty(entity.getPty())
                .reh(entity.getReh())
                .sky(entity.getSky())
                .tmp(entity.getTmp())
                .wsd(entity.getWsd())
                .tmn(tmn)
                .tmx(tmx)
                .beforeTmx(beforeTmx)
                .build();
        return dto;
    }

    default WeatherShortMainDTO entityToDTO(WeatherShortMain entity) {
        WeatherShortMainDTO dto = WeatherShortMainDTO.builder()
                .xyId(entity.getId().getWeatherXY().getId())
                .fcstDateTime(entity.getId().getFcstDateTime())
                .pop(entity.getPop())
                .pty(entity.getPty())
                .reh(entity.getReh())
                .sky(entity.getSky())
                .tmp(entity.getTmp())
                .wsd(entity.getWsd())
                .build();
        return dto;
    }

    default WeatherShortSubDTO subEntityToDTO(WeatherShortSub entity) {
        WeatherShortSubDTO dto = WeatherShortSubDTO.builder()
                .xyId(entity.getId().getWeatherXY().getId())
                .fcstDateTime(entity.getId().getFcstDateTime())
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
