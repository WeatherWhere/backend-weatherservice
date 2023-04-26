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


    @Cacheable("xylist")
    @Transactional
    /**
     * db에 있는 모든 지역의 격자 x,y 불러와서 list에 저장한 뒤 캐싱
     *
     * @return xyList 리턴
     */
    List<WeatherXY> splitXyList() throws Exception;

    /**
     * 컨트롤러에서 실질적으로 호출하는 메서드로 이 메서드 내에서 xylist를 불러온 뒤 병렬처리를 하여
     * 하나의 격자 x,y 좌표마다 파싱된 날씨 데이터를 mainEntityList, subEntityList에 담아 batch 메서드로 리턴하는 메서드
     *
     * @return weatherShortEntityListDTO에 mainEntityList, subEntityList를 담아 batch 메서드로 리턴, 실패시 예외 처리
     * @throws Exception
     */
    WeatherShortEntityListDTO getXYListWeatherAllSave() throws Exception;
}
