package com.weatherwhere.weatherservice.service.tour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortSubRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {

    private final WeatherShortMainRepository weatherShortMainRepository;
    private final WeatherShortSubRepository weatherShortSubRepository;

    private final ParseRankLocationCSVService parseRankLocationCSVService;

    private RankWeatherShortMainDTO findDataFromDb(RankLocationNXYDTO locationDTO, LocalDate searchDate){
        List<Double[]> avgTmpAndReh = weatherShortMainRepository.findAvgTmpAndRehById(locationDTO.getWeatherX(), locationDTO.getWeatherY(), searchDate);
        Double avgTmp = avgTmpAndReh.get(0)[0];
        Double avgReh = avgTmpAndReh.get(0)[1];

        Double maxTmp = weatherShortMainRepository.findTmxIdByIdXAndIdYAndDateRange(locationDTO.getWeatherX(),
            locationDTO.getWeatherY(), searchDate);



        Double maxReh = weatherShortMainRepository.findRehxIdByIdXAndIdYAndDateRange(locationDTO.getWeatherX(),
            locationDTO.getWeatherY(), searchDate);


        LocalTime time1 = LocalTime.of(6, 0); // 6시
        LocalTime time2 = LocalTime.of(18, 0); // 18시
        LocalDateTime startDate = searchDate.atTime(time1);
        LocalDateTime endDate = searchDate.atTime(time2);
        Double avgSky = weatherShortMainRepository.findAvgSkyById(locationDTO.getWeatherX(),
            locationDTO.getWeatherY(), startDate, endDate);

        Double sumPcp = weatherShortSubRepository.findSumPcpById(locationDTO.getWeatherX(),
            locationDTO.getWeatherY(), startDate, endDate);

        Double avgWsd = weatherShortMainRepository.findAvgWsdById(locationDTO.getWeatherX(),
            locationDTO.getWeatherY(), startDate, endDate);


        return RankWeatherShortMainDTO.builder()
            .level1(locationDTO.getLevel1())
            .level2(locationDTO.getLevel2())
            .weatherX(locationDTO.getWeatherX())
            .weatherY(locationDTO.getWeatherY())
            .fcstDate(searchDate)
            .sumPcp(sumPcp)
            .avgSky(avgSky)
            .avgTmp(avgTmp)
            .avgWsd(avgWsd)
            .avgReh(avgReh)
            .maxTmp(maxTmp)
            .maxReh(maxReh)
            .build();
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
                log.info("searchDate : {}", searchDate);
                try {
                    RankWeatherShortMainDTO rankWeatherShortMainDTO = findDataFromDb(dto, searchDate);
                    log.info("rankWeatherShortMainDTO : {}", rankWeatherShortMainDTO);
                    list.add(rankWeatherShortMainDTO);
                } catch (Exception e) {
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
        return ResultDTO.of(HttpStatus.OK.value(),"Rank 날씨 단기예보를 조회하는데 성공하였습니다.", list);
    }
}
