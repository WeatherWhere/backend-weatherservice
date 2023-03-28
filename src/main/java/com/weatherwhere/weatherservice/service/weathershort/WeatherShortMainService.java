package com.weatherwhere.weatherservice.service.weathershort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.WeatherShortAllDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.dto.WeatherShortSubDTO;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

public interface WeatherShortMainService {


    //dto리스트를 entity리스트로 변환한 뒤 db에 save하는 메서드
    //컨트롤러에서 최종적으로 이 service를 호출함.
    String getWeatherShortEntity(WeatherShortRequestDTO weatherShortRequestDTO) throws URISyntaxException, JsonProcessingException;

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
                .weatherXY(dto.getWeatherXY())
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
                .weatherXY(dto.getWeatherXY())
                .build();
        return weatherShortSub;
    }





}
