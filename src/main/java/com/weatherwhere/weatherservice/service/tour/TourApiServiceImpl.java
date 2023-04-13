package com.weatherwhere.weatherservice.service.tour;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.tour.RankLocationNXYDTO;
import com.weatherwhere.weatherservice.dto.tour.RankWeatherShortMainDTO;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortMainRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {

    private final WeatherShortMainRepository weatherShortMainRepository;

    private final ParseRankLocationCSVService parseRankLocationCSVService;

    // 날씨 예보와 Rank에 지역 정보 합치기
    private RankWeatherShortMainDTO combineRankData(Object[] shortMain, RankLocationNXYDTO dto, Double tmn, Double tmx, LocalDate searchDate) {
        RankWeatherShortMainDTO shortMainDTO = RankWeatherShortMainDTO.builder()
            .level1(dto.getLevel1())
            .level2(dto.getLevel2())
            .weatherX(dto.getWeatherX())
            .weatherY(dto.getWeatherY())
            .fcstDate(searchDate)
            .pop((double)shortMain[2])
            .pty((double)shortMain[3])
            .sky((double)shortMain[4])
            .tmp((double)shortMain[5])
            .wsd((double)shortMain[6])
            .reh((double)shortMain[7])
            .tmn(tmn)
            .tmx(tmx)
            .build();

        return shortMainDTO;
    }


    // 전체 찾아야할 격자 x, y로 날씨 단기 예보 DB에서 조회하기
    private List<RankWeatherShortMainDTO> makeRankData(List<RankLocationNXYDTO> locationDTOList){
        LocalDate now = LocalDate.now();
        List<RankWeatherShortMainDTO> list = new ArrayList<>();
        // 하루만 찾아준다. (3일로 수정 가능성 있어서)
        for (int i = 0 ; i < 1 ; i++) {
            LocalDate searchDate = now.plusDays(i);
            for (RankLocationNXYDTO dto : locationDTOList) {
                // DB에서 찾기
                log.info("date : {}", searchDate);
                try {
                    Object [] shortMain = weatherShortMainRepository
                        .findAveragesByCreatedAt(dto.getWeatherX(), dto.getWeatherY(), searchDate).get(0);
                    Double tmn = weatherShortMainRepository.findTmnIdByIdXAndIdYAndDateRange(dto.getWeatherX(), dto.getWeatherY(), searchDate).get(0);
                    Double tmx = weatherShortMainRepository.findTmxIdByIdXAndIdYAndDateRange(dto.getWeatherX(), dto.getWeatherY(), searchDate).get(0);

                    log.info("tmn : {}", tmn);
                    log.info("tmx : {}", tmx);

                    // locationDTO랑 shortMainDTO 합치기
                    RankWeatherShortMainDTO shortMainDTO = combineRankData(shortMain, dto, tmn, tmx, searchDate);
                    log.info("해당 날짜, 해당 격자 xy 단기예보 리스트 : {}", shortMainDTO);
                    list.add(shortMainDTO);
                } catch (Exception e){
                    e.printStackTrace();
                }
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
