package com.weatherwhere.weatherservice.repository.weathershort;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortCompositeKey;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.repository.weathershort.mapping.TmnMapping;
import com.weatherwhere.weatherservice.repository.weathershort.mapping.TmxMapping;

public interface WeatherShortMainRepository extends JpaRepository<WeatherShortMain, WeatherShortCompositeKey> {

/*
    WeatherShortMain findByFcstDateTimeAndWeatherXY(LocalDateTime fcstDateTime, WeatherXY weatherXY);

    @Query("SELECT m FROM WeatherShortMain m JOIN WeatherXY s ON m.id = s.id WHERE s.weatherX = :nx AND s.weatherY = :ny AND m.fcstDateTime = :fcstDateTime")
    WeatherShortMain findByWeatherXWeatherYAndFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
*/

    //복합키에 있는 WeatherXY객체의 WeatherX, WeatherY값과 fcstDateTime으로 데이터 불러오는 쿼리
    WeatherShortMain findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);

    //@Query("SELECT n.tmn FROM WeatherShortMain n Where n.fcstDateTime = :fcstDateTime AND n.weatherXY = :weatherXY ")
    TmnMapping findTmnByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);
    TmxMapping findTmxByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTime(Integer nx, Integer ny, LocalDateTime fcstDateTime);

    // tour
    List<WeatherShortMain> findByIdWeatherXYWeatherXAndIdWeatherXYWeatherYAndIdFcstDateTimeBetween(Integer nx, Integer ny, LocalDateTime startDateTime, LocalDateTime endDateTime);

}