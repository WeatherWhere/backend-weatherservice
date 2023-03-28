package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface WeatherMidService {
    Object getWeatherMidTa(String regId, String tmFc) throws ParseException;

    Object getWeatherMidLandFcst(String regId, String tmFc) throws ParseException;

    List<WeatherMidEntity> makeEntityList(JSONObject jsonFromMidTa, JSONObject jsonFromMidLandFcst, String[] daysAfterToday);
    List<WeatherMidCompositeKey> updateWeatherMid(String regId, String tmfc) throws ParseException;

    List<WeatherMidDTO> getMidForecast(String regionCode);

    // DTO를 Entity로 변환해주는 메서드
    default WeatherMidEntity dtoToEntity(WeatherMidDTO dto) {
        WeatherMidCompositeKey weatherMidCompositeKey = new WeatherMidCompositeKey(dto.getRegionCode(), dto.getBaseTime());
        WeatherMidEntity entity = WeatherMidEntity.builder()
                .id(weatherMidCompositeKey)
                .tmn(dto.getTmn())
                .tmx(dto.getTmx())
                .rAm(dto.getRAm())
                .rPm(dto.getRPm())
                .wAm(dto.getWAm())
                .wPm(dto.getWPm())
                .build();
        return entity;
    }

    // entity를 dto로 변환해주는 메서드
    // 전부 옮겨주어야 한다.
    default WeatherMidDTO entityToDTO(WeatherMidEntity entity) {
        WeatherMidDTO dto = WeatherMidDTO.builder()
                .regionCode(entity.getId().getRegionCode())
                .baseTime(entity.getId().getBaseTime())
                .tmn(entity.getTmn())
                .tmx(entity.getTmx())
                .rAm(entity.getRAm())
                .rPm(entity.getRPm())
                .wAm(entity.getWAm())
                .wPm(entity.getWPm())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
}
