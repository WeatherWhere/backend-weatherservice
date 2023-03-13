package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, Long> {
}