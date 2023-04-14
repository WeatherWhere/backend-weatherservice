package com.weatherwhere.weatherservice.repository.weathershort;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, WeatherShortCompositeKey> {

    /*
        WeatherShortSub findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

        @Query("SELECT wsm FROM WeatherShortSub wsm JOIN wsm.weatherXY wx WHERE wx.weatherX = :nx AND wx.weatherY = :ny AND wsm.fcstDateTime = :fcstDateTime")
        WeatherShortSub findByWeatherXWeatherYAndFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
    */
    WeatherShortSub findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);

    // 관광
    // 해당 격자 x, y 값과 해당 날짜에 해당하는 값 조회

    // 6 ~ 18시 누적 강수량
    @Query("SELECT SUM(CAST(CASE WHEN w.pcp = '강수없음' THEN '0' ELSE SUBSTR(w.pcp, 1, LENGTH(w.pcp) - 2) END AS DOUBLE)) " +
        "FROM WeatherShortSub w " +
        "WHERE w.id.weatherXY.weatherX = :x " +
        "  AND w.id.weatherXY.weatherY = :y " +
        "  AND w.id.fcstDateTime BETWEEN :startDateTime AND :endDateTime")
    Double findSumPcpById (
        @Param("x") Integer x,
        @Param("y") Integer y,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime);


}

