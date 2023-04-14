package com.weatherwhere.weatherservice.repository.weathershort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.repository.weathershort.mapping.TmnMapping;
import com.weatherwhere.weatherservice.repository.weathershort.mapping.TmxMapping;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, WeatherShortCompositeKey> {

/*
    WeatherShortMain findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT m FROM WeatherShortMain m JOIN WeatherXY s ON m.id = s.id WHERE s.weatherX = :nx AND s.weatherY = :ny AND m.fcstDateTime = :fcstDateTime")
    WeatherShortMain findByWeatherXWeatherYAndFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
*/

    //복합키에 있는 WeatherXY객체의 WeatherX, WeatherY값과 fcstDateTime으로 데이터 불러오는 쿼리
    WeatherShortMain findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny,
        LocalDateTime fcstDateTime);

    //@Query("SELECT n.tmn FROM WeatherShortMain n Where n.fcstDateTime = :fcstDateTime AND n.weatherXY = :weatherXY ")
    TmnMapping findTmnByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny,
        LocalDateTime fcstDateTime);

    TmxMapping findTmxByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny,
        LocalDateTime fcstDateTime);


    // 관광
    // 해당 격자 x, y 값과 해당 날짜에 해당하는 값 조회

    // 일평균기온과 일평균습도
    @Query("SELECT AVG(w.tmp), AVG(w.reh) "
        + "FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x AND "
        + "w.id.weatherXY.weatherY = :y AND DATE(w.id.fcstDateTime) = :searchDate ")
    List<Double[]> findAvgTmpAndRehById (@Param("x") Integer x, @Param("y") Integer y, @Param("searchDate") LocalDate searchDate);

    // 최고기온
    @Query("SELECT MAX(w.tmx) as tmx FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x "
        + "AND w.id.weatherXY.weatherY = :y AND DATE(w.id.fcstDateTime) = :searchDate AND w.tmx IS NOT NULL GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    Double findTmxIdByIdXAndIdYAndDateRange (@Param("x") Integer x, @Param("y") Integer y,
    @Param("searchDate") LocalDate searchDate);

    // 최소 습도
    @Query("SELECT MIN(w.reh) as rehn FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x "
        + "AND w.id.weatherXY.weatherY = :y AND DATE(w.id.fcstDateTime) = :searchDate GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    Double findRehnIdByIdXAndIdYAndDateRange(@Param("x") Integer x, @Param("y") Integer y,
        @Param("searchDate") LocalDate searchDate);

    // 6 ~ 18시 평균 하늘 상태(운량을 위해)
    @Query("SELECT AVG(w.sky) FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x AND "
        + "w.id.weatherXY.weatherY = :y AND w.id.fcstDateTime BETWEEN :startDateTime AND :endDateTime "
        + "GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    Double findAvgSkyById (
        @Param("x") Integer x,
        @Param("y") Integer y,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime);

    // 6 ~ 18시 평균 풍속
    @Query("SELECT AVG(w.wsd) FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x AND "
        + "w.id.weatherXY.weatherY = :y AND w.id.fcstDateTime BETWEEN :startDateTime AND :endDateTime "
        + "GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    Double findAvgWsdById (
        @Param("x") Integer x,
        @Param("y") Integer y,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime);
}