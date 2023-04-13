package com.weatherwhere.weatherservice.repository.weathershort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.dto.tour.ShortMainDTO;
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


    // tour
    List<WeatherShortMain> findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTimeBetween(Integer nx,
        Integer ny, LocalDateTime startDateTime, LocalDateTime endDateTime);


    @Query("SELECT w.id.weatherXY.weatherX, w.id.weatherXY.weatherY, "
        + "AVG(w.pop), AVG(w.pty), AVG(w.sky), AVG(w.tmp), AVG(w.wsd), AVG(w.reh) "
        + "FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x AND "
        + "w.id.weatherXY.weatherY = :y AND DATE(w.id.fcstDateTime) = :searchDate  "
        + "GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    List<Object []> findAveragesByCreatedAt(
        @Param("x") Integer x,
        @Param("y") Integer y,
        @Param("searchDate") LocalDate searchDate);

    // 최저기온
    @Query("SELECT AVG(w.tmn) as tmn FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x "
        + "AND w.id.weatherXY.weatherY = :y AND DATE(w.id.fcstDateTime) = :searchDate  AND w.tmn IS NOT NULL GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    List<Double> findTmnIdByIdXAndIdYAndDateRange(@Param("x") Integer x, @Param("y") Integer y,
        @Param("searchDate") LocalDate searchDate);

    // 최고기온
    @Query("SELECT AVG(w.tmx) as tmx FROM WeatherShortMain w WHERE w.id.weatherXY.weatherX = :x "
        + "AND w.id.weatherXY.weatherY = :y AND DATE(w.id.fcstDateTime) = :searchDate AND w.tmx IS NOT NULL GROUP BY w.id.weatherXY.weatherY, w.id.weatherXY.weatherX")
    List<Double> findTmxIdByIdXAndIdYAndDateRange(@Param("x") Integer x, @Param("y") Integer y,
    @Param("searchDate") LocalDate searchDate);

}