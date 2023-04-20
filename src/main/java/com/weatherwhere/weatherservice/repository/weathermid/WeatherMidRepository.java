package com.weatherwhere.weatherservice.repository.weathermid;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidCompositeKey;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import org.springframework.data.repository.query.Param;

public interface WeatherMidRepository extends JpaRepository<WeatherMidEntity, WeatherMidCompositeKey> {
    Optional<WeatherMidEntity> findById(WeatherMidCompositeKey weatherMidCompositeKey);

    Optional<WeatherMidEntity> findByRegionNameAndIdBaseTime(String regionName, String baseTime);

    Optional<WeatherMidEntity> findByRegionNameAndIdBaseTimeAndCity(String regionName, String baseTime, String city);


}
