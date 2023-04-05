//package com.weatherwhere.weatherservice.config;
//
//import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
//import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
//import com.weatherwhere.weatherservice.dto.weathermid.WeatherMidDTO;
//import com.weatherwhere.weatherservice.service.date.DateService;
//import com.weatherwhere.weatherservice.service.weathermid.ParseCSVService;
//import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Log4j2
//@Configuration
//@RequiredArgsConstructor
//public class BatchConfiguration {
//    // 필요한 2개의 서비스를 생성자 주입
//    private final WeatherMidService weatherMidService;
//    private final ParseCSVService parseCSVService;
//    private final DateService dateService;
//
//    private List<RegionCodeDTO> regionCodeDTOList = parseCSVService.ParseCSV();
//    private String[] threeToSevenDays = dateService.getDaysAfterToday(3, 7);
//    private List<WeatherMidEntity> weatherMidEntities = weatherMidService.makeEntityList(regionCodeDTOList, threeToSevenDays, "202304041800");
//
//
//}
