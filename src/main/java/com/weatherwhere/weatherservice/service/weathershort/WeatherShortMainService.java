package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;

import java.net.URISyntaxException;

public interface WeatherShortMainService {


    //dto리스트를 entity리스트로 변환한 뒤 db에 save하는 메서드
    //컨트롤러에서 최종적으로 이 service를 호출함.
    String getWeatherShortEntity(WeatherShortRequestDTO weatherShortRequestDTO) throws Exception;

    //String readWeatherXYLocation() throws IOException;

    // 단기예보 메인 DTO -> Entity
    default WeatherShortMain mainDtoToEntity(WeatherShortAllDTO dto) {
        WeatherShortMain weatherShortMain = WeatherShortMain.builder()
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .fcstDateTime(dto.getFcstDateTime())
                .pop(dto.getPop())
                .pty(dto.getPty())
                .reh(dto.getReh())
                .sky(dto.getSky())
                .tmp(dto.getTmp())
                .wsd(dto.getWsd())
                .tmn(dto.getTmn())
                .tmx(dto.getTmx())
                .weatherXY(dto.getWeatherXY())
                .build();
        return weatherShortMain;
    }

    //단기예보 서브 DTO -> Entity
    default WeatherShortSub subDtoToEntity(WeatherShortAllDTO dto) {
        WeatherShortSub weatherShortSub = WeatherShortSub.builder()
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .fcstDateTime(dto.getFcstDateTime())
                .pcp(dto.getPcp())
                .sno(dto.getSno())
                .uuu(dto.getUuu())
                .vvv(dto.getVvv())
                .wav(dto.getWav())
                .vec(dto.getVec())
                .weatherXY(dto.getWeatherXY())
                .build();
        return weatherShortSub;
    }





}
