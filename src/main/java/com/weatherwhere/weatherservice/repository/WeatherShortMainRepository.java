package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, Long> {

    WeatherShortMain findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);


}