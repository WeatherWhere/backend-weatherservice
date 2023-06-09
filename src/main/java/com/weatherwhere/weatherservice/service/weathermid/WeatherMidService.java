package com.weatherwhere.weatherservice.service.weathermid;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface WeatherMidService {
    Object getWeatherMidTa(String regId, String tmFc) throws ParseException;

    Object getWeatherMidLandFcst(String regId, String tmFc) throws ParseException;

    List<WeatherMidEntity> makeEntityList(List<RegionCodeDTO> regionCodeDTOList, String[] threeToSevenDays, String tmfc);

    List<WeatherMidCompositeKey> updateWeatherMid(List<WeatherMidEntity> weatherMidEntityList);

    ResultDTO<List<WeatherMidDTO>> getMidForecast(String regionCode);

    // DTO를 Entity로 변환해주는 메서드
    default WeatherMidEntity dtoToEntity(WeatherMidDTO dto) {
        WeatherMidCompositeKey weatherMidCompositeKey = new WeatherMidCompositeKey(dto.getRegionCode(), dto.getBaseTime());
        WeatherMidEntity entity = WeatherMidEntity.builder()
                .id(weatherMidCompositeKey)
                .regionName(dto.getRegionName())
                .city(dto.getCity())
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
                .regionName(entity.getRegionName())
                .city(entity.getCity())
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

    //행정동 주소로 mid data 조회
    ResultDTO<List<WeatherMidDTO>> getMidForecastAddress(String region1, String region2);
}
