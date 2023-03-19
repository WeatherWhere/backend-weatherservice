package com.weatherwhere.weatherservice.repository;

import com.weatherwhere.weatherservice.domain.WeatherShortMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, Long> {

    WeatherShortMain findByFcstDateAndFcstTime(String fcstDate, String fcstTime);

}