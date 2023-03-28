package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, Long> {

    WeatherShortSub findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);



}

