package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, Long> {

    WeatherShortMain findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);


}