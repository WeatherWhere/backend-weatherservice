package com.weatherwhere.weatherservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface WeatherShortMainService {

    JsonNode weatherShortJsonParsing(String baseDate, String baseTime, String nx, String ny) throws JsonProcessingException, URISyntaxException;

    //단기예보 api 받아서 dto에 저장한 뒤 entity로 변환하고 db에 save하는 서비스
    //override를 안해도 오류가 발생하지 않지만 해야 컴파일할떄 버그를 쉽게 찾을 수 있음.
    //impl의 소스코드와 연결되어 있다는 걸 뜻함.
    List<WeatherShortMainDto> getWeatherShortDto(String nx, String ny, String baseDate, String baseTime) throws URISyntaxException, JsonProcessingException;

    public String readWeatherXYLocation() throws IOException;

    // DTO -> Entity
    default WeatherShortMain dtoToEntity(WeatherShortMainDto dto) {
        WeatherShortMain weatherShortMain = WeatherShortMain.builder()
                .weatherX(dto.getWeatherX())
                .weatherY(dto.getWeatherY())
                .baseDate(dto.getBaseDate())
                .baseTime(dto.getBaseTime())
                .fcstDate(dto.getFcstDate())
                .fcstTime(dto.getFcstTime())
                .pop(dto.getPop())
                .pcp(dto.getPcp())
                .pty(dto.getPty())
                .reh(dto.getReh())
                .sky(dto.getSky())
                .tmp(dto.getTmp())
                .wsd(dto.getWsd())
                .build();
        return weatherShortMain;
    }


}
