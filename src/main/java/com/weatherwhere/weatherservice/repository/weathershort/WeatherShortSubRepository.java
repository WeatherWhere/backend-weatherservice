package com.weatherwhere.weatherservice.repository.weathershort;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, WeatherShortCompositeKey> {

    /*
        WeatherShortSub findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

        @Query("SELECT wsm FROM WeatherShortSub wsm JOIN wsm.weatherXY wx WHERE wx.weatherX = :nx AND wx.weatherY = :ny AND wsm.fcstDateTime = :fcstDateTime")
        WeatherShortSub findByWeatherXWeatherYAndFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
    */
    WeatherShortSub findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);


}

