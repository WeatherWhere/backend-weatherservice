package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, Long> {

    WeatherShortSub findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);


}

