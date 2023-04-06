package com.weatherwhere.weatherservice.service.tour;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.weathermid.WeatherMidRepository;
import com.weatherwhere.weatherservice.service.tour.TourApiService;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {
    private final WeatherMidRepository weatherMidRepository;
    private final WeatherMidService weatherMidService;
    @Override
    @Transactional
    public ResultDTO<WeatherMidDTO> getWeatherMidData(String regionCode, String baseDate) {
        WeatherMidCompositeKey weatherMidCompositeKey = new WeatherMidCompositeKey(regionCode,baseDate);
        try {
            WeatherMidEntity weatherMidEntity = weatherMidRepository.findById(weatherMidCompositeKey)
                .orElseThrow(() -> new NoSuchElementException());
            WeatherMidDTO data=weatherMidService.entityToDTO(weatherMidEntity);

            log.info("날씨 중기예보: {}",data);
            return ResultDTO.of(HttpStatus.OK.value(),"날씨 중기예보를 조회하는데 성공하였습니다.",data);
        } catch (NoSuchElementException e) {
            // db에서 찾는 데이터 없을 경우
            e.getStackTrace();
            log.error("db에 날씨 중기예보 데이터 없음");
            // 수정해야함
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            // 수정해야함
            return null;
        }
    }
}
