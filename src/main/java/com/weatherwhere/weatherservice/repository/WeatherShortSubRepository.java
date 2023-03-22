package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.WeatherShortSub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherShortSubRepository extends JpaRepository<WeatherShortSub, Long> {

    WeatherShortSub findByFcstDateAndFcstTime(String fcstDate, String fcstTime);


}

