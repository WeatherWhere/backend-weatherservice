package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, Long> {

    WeatherShortMain findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT n.tmn FROM WeatherShortMain n Where n.fcstDateTime = :fcstDateTime AND n.weatherXY = :weatherXY ")
    Double findTmnByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT n.tmx FROM WeatherShortMain n Where n.fcstDateTime = :fcstDateTime AND n.weatherXY = :weatherXY ")
    Double findTmxByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

}