package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, WeatherShortCompositeKey> {

/*
    WeatherShortSub findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT wsm FROM WeatherShortSub wsm JOIN wsm.weatherXY wx WHERE wx.weatherX = :nx AND wx.weatherY = :ny AND wsm.fcstDateTime = :fcstDateTime")
    WeatherShortSub findByWeatherXWeatherYAndFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
*/


}

