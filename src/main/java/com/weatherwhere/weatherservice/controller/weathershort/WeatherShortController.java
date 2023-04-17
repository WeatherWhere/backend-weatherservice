package com.weatherwhere.weatherservice.controller.weathershort;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.tour.RankWeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.service.tour.TourApiService;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherShortController {

    private final WeatherShortMainService weatherShortMainService;
    private final WeatherShortMainApiService weatherShortMainApiService;
    private final TourApiService tourApiService;

/*
    //nx, ny별 단기예보 데이터 저장하는 api
    @GetMapping("/forecast/short")
    public String weatherShortMainEntityList(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {
        try {

            List<WeatherShortMain> mainEntityList = Collections.synchronizedList(new ArrayList<>());
            List<WeatherShortSub> subEntityList = Collections.synchronizedList(new ArrayList<>());

            return weatherShortMainService.getXYListWeatherAllSave(weatherShortRequestDTO, mainEntityList, subEntityList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);

        }
    }*/

    //단기예보 메인 api
    @GetMapping("/forecast/short/main")
    public ResultDTO<Object> getWeatherShortMainResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO);
    }

    //단기예보 메인(현재시간) api
    @GetMapping("/forecast/short/main/now")
    public ResultDTO<Object> getWeatherShortMainNowResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
         return weatherShortMainApiService.getWeatherShortMainNowData(weatherShortMainApiRequestDTO);
    }



    //단기예보 서브 api
    @GetMapping("/forecast/short/sub")
    public ResultDTO<Object> getWeatherShortSubResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortSubData(weatherShortMainApiRequestDTO);
    }

    @GetMapping(value = "/tour/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDTO<List<RankWeatherShortMainDTO>> getRankWeatherData() {
        ResultDTO<List<RankWeatherShortMainDTO>> list = tourApiService.getRankWeatherShortMainData();
        return list;
    }
}
