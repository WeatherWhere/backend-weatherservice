package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherXYRepository extends JpaRepository<WeatherXY, Long> {

    WeatherXY findByWeatherXAndWeatherY(Integer nx, Integer ny);

    @Query("SELECT wx.weatherX, wy.weatherY FROM WeatherXY wx, WeatherXY wy WHERE wx.id = wy.id")
    List<Object[]> findAllNxAndNy();
}
