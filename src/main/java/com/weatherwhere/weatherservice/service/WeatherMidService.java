package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface WeatherMidService {
    Object getWeatherMidTa(String regId, String tmFc) throws ParseException;

    Object getWeatherMidLandFcst(String regId, String tmFc) throws ParseException;

    Long register(WeatherMidDTO dto);

    List<WeatherMidDTO> makeDTOList(JSONObject jsonFromMidTa, JSONObject jsonFromMidLandFcst, String[] daysAfterToday);
    List<Long> updateWeatherMid(String regId, String tmfc) throws ParseException;


    // DTO를 Entity로 변환해주는 메서드
    default WeatherMidEntity dtoToEntity(WeatherMidDTO dto) {
        // 삽입 날짜와 수정 날짜는 entity가 삽입되거나 수정될 때 생성되므로 옮겨줄 필요가 없음.
        WeatherMidEntity entity = WeatherMidEntity.builder()
                .midTermForecastId(dto.getMidTermForecastId())
                .baseTime(dto.getBaseTime())
                .tmn(dto.getTmn())
                .tmx(dto.getTmx())
                .regionCode(dto.getRegionCode())
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
                .midTermForecastId(entity.getMidTermForecastId())
                .baseTime(entity.getBaseTime())
                .tmn(entity.getTmn())
                .tmx(entity.getTmx())
                .regionCode(entity.getRegionCode())
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
