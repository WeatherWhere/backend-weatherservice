package com.weatherwhere.weatherservice.service;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.WeatherMidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherMidServiceImpl implements WeatherMidService {
    private final WeatherMidRepository weatherMidRepository;

    public Long register(WeatherMidDTO dto) {
        // 파라미터가 제대로 넘어오는지 확인
        log.info("삽입 데이터:" + dto.toString());

        // repository의 메서드 매개변수로 변경
        WeatherMidEntity entity = dtoToEntity(dto);
        // 이 시점에는 entity에 mid_term_forecast_id와 regDate, modDate는 없고,
        // save를 할 때 설정한 내역을 가지고 데이터를 설정
        weatherMidRepository.save(entity);
        return entity.getMid_term_forecast_id();
    }
}
