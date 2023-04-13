package com.weatherwhere.weatherservice.service.tour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.tour.RankLocationNXYDTO;
import com.weatherwhere.weatherservice.dto.tour.RankWeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.repository.weathermid.WeatherMidRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortMainRepository;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {
    private final WeatherShortMainRepository weatherShortMainRepository;

    private final ParseRankLocationCSVService parseRankLocationCSVService;

    // entity List -> DTO List
    private List<WeatherShortMainDTO> entityToDTOList(List<WeatherShortMain> list) {
        List<WeatherShortMainDTO> dtoList = new ArrayList<>();
        for(WeatherShortMain entity : list){
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    // 날씨 예보와 Rank에 지역 정보 합치기
    private List<RankWeatherShortMainDTO> combineRankData(List<WeatherShortMainDTO> mainDTOList, RankLocationNXYDTO locationDTO) {
        List<RankWeatherShortMainDTO> list = new ArrayList<>();
        for (WeatherShortMainDTO mainDTO : mainDTOList) {
            RankWeatherShortMainDTO dto = RankWeatherShortMainDTO.builder()
                .level1(locationDTO.getLevel1())
                .level2(locationDTO.getLevel2())
                .sky(mainDTO.getSky())
                .tmn(mainDTO.getTmn())
                .tmp(mainDTO.getTmp())
                .tmx(mainDTO.getTmx())
                .weatherY(locationDTO.getWeatherY())
                .weatherX(locationDTO.getWeatherX())
                .wsd(mainDTO.getWsd())
                .pop(mainDTO.getPop())
                .pty(mainDTO.getPty())
                .reh(mainDTO.getReh())
                .fcstDateTime(mainDTO.getFcstDateTime())
                .build();
            list.add(dto);
        }
        return list;
    }


    // 전체 찾아야할 격자 x, y로 날씨 단기 예보 DB에서 조회하기
    private List<RankWeatherShortMainDTO> makeRankData(List<RankLocationNXYDTO> locationDTOList){
        LocalDate now = LocalDate.now();
        List<RankWeatherShortMainDTO> list = new ArrayList<>();
        for (int i = 0 ; i < 1 ; i++) {
            LocalDate searchDate = now.plusDays(i);
            for (RankLocationNXYDTO dto : locationDTOList) {
                // DB에서 찾기
                List<WeatherShortMain> weatherShortMainList = weatherShortMainRepository
                    .findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTimeBetween(dto.getWeatherX(), dto.getWeatherY(), searchDate.atStartOfDay(), searchDate.atTime(
                        LocalTime.MAX));
                log.info("해당 날짜의 해당 격자 xy 리스트 : {}", weatherShortMainList);
                list.addAll(combineRankData(entityToDTOList(weatherShortMainList), dto));
            }
        }
        return list;
    }



    @Override
    @Transactional
    public ResultDTO<List<RankWeatherShortMainDTO>> getRankWeatherShortMainData() {
        List<RankWeatherShortMainDTO> list = new ArrayList<>();
        List<RankLocationNXYDTO> locationList = parseRankLocationCSVService.ParseRankLovationCSV();
        try {
            list = makeRankData(locationList);
            log.info("rank 전체 데이터 리스트 : {}",list);
        } catch (NoSuchElementException e) {
            // db에서 찾는 데이터 없을 경우
            e.getStackTrace();
            log.error("db에 날씨 단기예보 데이터 없음");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResultDTO.of(HttpStatus.OK.value(),"Rank 날씨 단기예보를 조회하는데 성공하였습니다.",list);
    }
}
