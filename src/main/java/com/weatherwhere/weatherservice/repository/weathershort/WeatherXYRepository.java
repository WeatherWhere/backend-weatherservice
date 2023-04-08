package com.weatherwhere.weatherservice.repository.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherXYRepository extends JpaRepository<WeatherXY, Long> {

    WeatherXY findByWeatherXAndWeatherY(Integer nx, Integer ny);
/*

    List<WeatherXY> findByWeatherShortMainListFcstDateTimeAndWeatherXAndWeatherY(LocalDateTime fcstDateTime, Integer nx, Integer ny);
*/

    //xy리스트 불러오기
    List<WeatherXY> findAll();


}
