package com.weatherwhere.weatherservice.repository.weathermid;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeatherMidRepository extends JpaRepository<WeatherMidEntity, WeatherMidCompositeKey> {
    Optional<WeatherMidEntity> findById(WeatherMidCompositeKey weatherMidCompositeKey);
    List<WeatherMidEntity> findByIdBaseTime(String baseTime);
}
