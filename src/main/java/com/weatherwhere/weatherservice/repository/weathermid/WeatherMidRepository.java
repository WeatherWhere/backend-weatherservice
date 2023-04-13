package com.weatherwhere.weatherservice.repository.weathermid;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;

public interface WeatherMidRepository extends JpaRepository<WeatherMidEntity, WeatherMidCompositeKey> {
    Optional<WeatherMidEntity> findById(WeatherMidCompositeKey weatherMidCompositeKey);
}
