package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherMidRepository extends JpaRepository<WeatherMidEntity, Long> {
}
