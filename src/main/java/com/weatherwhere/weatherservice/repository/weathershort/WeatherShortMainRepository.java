package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, WeatherShortCompositeKey> {
/*
    WeatherShortMain findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);


    @Query("SELECT n.tmn FROM WeatherShortMain n Where n.fcstDateTime = :fcstDateTime AND n.weatherXY = :weatherXY ")
    Double findTmnByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT n.tmx FROM WeatherShortMain n Where n.fcstDateTime = :fcstDateTime AND n.weatherXY = :weatherXY ")
    Double findTmxByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT wsm FROM WeatherShortMain wsm JOIN wsm.weatherXY wx WHERE wx.weatherX = :nx AND wx.weatherY = :ny AND wsm.fcstDateTime = :fcstDateTime")
    WeatherShortMain findByWeatherXWeatherYAndFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
*/




}