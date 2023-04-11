package com.weatherwhere.weatherservice.service.tour;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.repository.weathermid.WeatherMidRepository;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {
    private final WeatherMidRepository weatherMidRepository;
    private final WeatherMidService weatherMidService;
    private List<WeatherMidDTO> entityToDTOList(List<WeatherMidEntity> list) {
        List<WeatherMidDTO> dtoList = new ArrayList<>();
        for(WeatherMidEntity weatherMidEntity : list){
            dtoList.add(weatherMidService.entityToDTO(weatherMidEntity));
        }
        return dtoList;
    }
    @Override
    @Transactional
    public ResultDTO<List<WeatherMidDTO>> getWeatherMidData(String baseTime) {
        List<WeatherMidDTO> list = new ArrayList<>();
        try {
            List<WeatherMidEntity> weatherMidEntityList = weatherMidRepository.findByIdBaseTime(baseTime);
            list = entityToDTOList(weatherMidEntityList);
            log.info("날씨 중기예보: {}",list);
        } catch (NoSuchElementException e) {
            // db에서 찾는 데이터 없을 경우
            e.getStackTrace();
            log.error("db에 날씨 중기예보 데이터 없음");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResultDTO.of(HttpStatus.OK.value(),"날씨 중기예보를 조회하는데 성공하였습니다.",list);
    }
}
