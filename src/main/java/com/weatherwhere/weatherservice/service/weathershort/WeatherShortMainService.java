package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import jakarta.transaction.Transactional;

import java.net.URISyntaxException;
import java.util.List;

public interface WeatherShortMainService {


    // 단기예보 메인 DTO -> Entity
    default WeatherShortMain mainDtoToEntity(WeatherShortAllDTO dto, WeatherXY weatherXY) {
        WeatherShortCompositeKey weatherShortCompositeKey = new WeatherShortCompositeKey(dto.getFcstDateTime(), weatherXY);
        WeatherShortMain weatherShortMain = WeatherShortMain.builder()
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .pop(dto.getPop())
                .pty(dto.getPty())
                .reh(dto.getReh())
                .sky(dto.getSky())
                .tmp(dto.getTmp())
                .wsd(dto.getWsd())
                .tmn(dto.getTmn())
                .tmx(dto.getTmx())
                .id(weatherShortCompositeKey)
                .build();
        return weatherShortMain;
    }

    //단기예보 서브 DTO -> Entity
    default WeatherShortSub subDtoToEntity(WeatherShortAllDTO dto,WeatherXY weatherXY) {
        WeatherShortCompositeKey weatherShortCompositeKey = new WeatherShortCompositeKey(dto.getFcstDateTime(), weatherXY);
        WeatherShortSub weatherShortSub = WeatherShortSub.builder()
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .pcp(dto.getPcp())
                .sno(dto.getSno())
                .uuu(dto.getUuu())
                .vvv(dto.getVvv())
                .wav(dto.getWav())
                .vec(dto.getVec())
                .id(weatherShortCompositeKey)
                .build();
        return weatherShortSub;
    }

    String getXYListWeatherAllSave(WeatherShortRequestDTO weatherShortRequestDTO, List<WeatherShortMain> mainEntityList, List<WeatherShortSub> subEntityList) throws Exception;



}
