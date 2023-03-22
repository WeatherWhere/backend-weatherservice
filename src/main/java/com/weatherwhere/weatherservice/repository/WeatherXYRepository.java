package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherXYRepository extends JpaRepository<WeatherXY, Long> {

    WeatherXY findByWeatherXAndWeatherY(Integer nx, Integer ny);

}
