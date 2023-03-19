package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;

public interface WeatherMidService {
    public Long register(WeatherMidDTO dto);

    // DTO를 Entity로 변환해주는 메서드
    default WeatherMidEntity dtoToEntity(WeatherMidDTO dto) {
        // 삽입 날짜와 수정 날짜는 entity가 삽입되거나 수정될 때 생성되므로 옮겨줄 필요가 없음.
        WeatherMidEntity entity = WeatherMidEntity.builder()
                .mid_term_forecast_id(dto.getMid_term_forecast_id())
                .base_time(dto.getBase_time())
                .tmn(dto.getTmn())
                .tmx(dto.getTmx())
                .region_code(dto.getRegion_code())
                .r_am(dto.getR_am())
                .r_pm(dto.getR_pm())
                .w_am(dto.getW_am())
                .w_pm(dto.getW_pm())
                .build();
        return entity;
    }

    // entity를 dto로 변환해주는 메서드
    // 전부 옮겨주어야 한다.
    default WeatherMidDTO dtoToDTO(WeatherMidEntity entity) {
        WeatherMidDTO dto = WeatherMidDTO.builder()
                .mid_term_forecast_id(entity.getMid_term_forecast_id())
                .base_time(entity.getBase_time())
                .tmn(entity.getTmn())
                .tmx(entity.getTmx())
                .region_code(entity.getRegion_code())
                .r_am(entity.getR_am())
                .r_pm(entity.getR_pm())
                .w_am(entity.getW_am())
                .w_pm(entity.getW_pm())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
