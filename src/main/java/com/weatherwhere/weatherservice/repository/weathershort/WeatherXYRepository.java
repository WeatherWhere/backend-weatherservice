package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherXYRepository extends JpaRepository<WeatherXY, Long> {

    WeatherXY findByWeatherXAndWeatherY(Integer nx, Integer ny);

}
