package com.weatherwhere.weatherservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortSubDTO;

import java.io.IOException;
import java.net.URISyntaxException;

public interface WeatherShortMainService {

    String getWeatherShortEntity(String nx, String ny, String baseDate, String baseTime) throws URISyntaxException, JsonProcessingException;
    //String readWeatherXYLocation() throws IOException;

    // 단기예보 메인 DTO -> Entity
    default WeatherShortMain mainDtoToEntity(WeatherShortAllDTO dto) {
        WeatherShortMain weatherShortMain = WeatherShortMain.builder()
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .fcstDate(dto.getFcstDate())
                .fcstTime(dto.getFcstTime())
                .pop(dto.getPop())
                .pty(dto.getPty())
                .reh(dto.getReh())
                .sky(dto.getSky())
                .tmp(dto.getTmp())
                .wsd(dto.getWsd())
                .tmn(dto.getTmn())
                .tmx(dto.getTmx())
                .build();
        return weatherShortMain;
    }

    //단기예보 서브 DTO -> Entity
    default WeatherShortSub subDtoToEntity(WeatherShortAllDTO dto) {
        WeatherShortSub weatherShortSub = WeatherShortSub.builder()
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .fcstDate(dto.getFcstDate())
                .fcstTime(dto.getFcstTime())
                .pcp(dto.getPcp())
                .sno(dto.getSno())
                .uuu(dto.getUuu())
                .vvv(dto.getVvv())
                .wav(dto.getWav())
                .vec(dto.getVec())
                .build();
        return weatherShortSub;
    }





}
