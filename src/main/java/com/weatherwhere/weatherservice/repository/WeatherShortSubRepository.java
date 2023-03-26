package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherShortSub;
import com.weatherwhere.weatherservice.domain.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, Long> {

    WeatherShortSub findByFcstDateAndFcstTimeAndWeatherXY(LocalDate fcstDate, LocalTime fcstTime, WeatherXY weatherXY);


}

