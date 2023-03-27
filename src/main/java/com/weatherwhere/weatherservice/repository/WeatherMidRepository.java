package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.WeatherMidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherMidRepository extends JpaRepository<WeatherMidEntity, WeatherMidCompositeKey> {
    Optional<WeatherMidEntity> findById(WeatherMidCompositeKey weatherMidCompositeKey);
}
