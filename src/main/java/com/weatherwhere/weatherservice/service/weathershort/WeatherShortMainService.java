package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortEntityListDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;

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

    //xylist 잘게 쪼개는 메서드
    //xylist는 어플리케이션 실행 시 캐시에 저장해놓기 때문에 불필요한 select문을 줄임.
    @Cacheable("xylist")
    List<WeatherXY> splitXyList() throws Exception;


    //모든 xy리스트에 대한 값 저장하는 메서드
    WeatherShortEntityListDTO getXYListWeatherAllSave(WeatherShortRequestDTO weatherShortRequestDTO, List<WeatherShortMain> mainEntityList, List<WeatherShortSub> subEntityList) throws Exception;
}
