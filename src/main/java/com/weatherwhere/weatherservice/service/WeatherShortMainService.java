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

    String getWeatherShortEntity(String nx, String ny, String baseDate, String baseTime) throws URISyntaxException, JsonProcessingException;
    String readWeatherXYLocation() throws IOException;

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
