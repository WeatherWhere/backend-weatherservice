package com.weatherwhere.weatherservice.controller.weathershort;

import java.util.List;

import com.weatherwhere.weatherservice.dto.weathershort.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.tour.RankWeatherShortMainDTO;
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
    //deprecated
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


    /**
     * 단기예보 메인 12시간 api(사용자로부터 받은 위경도로 현재 시간부터 12시간 뒤까지의 날씨 메인 데이터 리턴)
     *
     * @param weatherShortRequestDTO
     * @return ResultDTO<List<WeatherShortMainDTO>>에 mainDataList 담아서 리턴, 실패시 statusCode=500과 data=null 리턴
     * @throws Exception
     */
    @GetMapping("/forecast/short/main")
    public ResultDTO<List<WeatherShortAllDTO>> getWeatherShortMainResponse(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortMainData(weatherShortRequestDTO);
    }


    /**
     * 단기예보 실시간 메인 api (사용자로부터 받은 위경도로 현재 시간의 날씨 데이터 리턴)
     * @param weatherShortRequestDTO
     * @return ResultDTO<WeatherShortMainDTO>에 mainData 담아서 리턴, 실패시 statusCode=500과 data=null 리턴
     * @throws Exception
     */
    @GetMapping("/forecast/short/main/now")
    public ResultDTO<WeatherShortAllDTO> getWeatherShortMainNowResponse(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception{
         return weatherShortMainApiService.getWeatherShortMainNowData(weatherShortRequestDTO);
    }


    /**
     * 단기예보 서브 12시간 api(사용자로부터 받은 위경도로 현재 시간부터 12시간 뒤까지의 날씨 서브 데이터 리턴)
     *
     * @param weatherShortRequestDTO
     * @return ResultDTO<List<WeatherShortSubDTO>>에 subDataList 담아서 리턴, 실패시 statusCode=500과 data=null 리턴
     * @throws Exception
     */
    @GetMapping("/forecast/short/sub")
    public ResultDTO<List<WeatherShortAllDTO>> getWeatherShortSubResponse(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception{
            return weatherShortMainApiService.getWeatherShortSubData(weatherShortRequestDTO);
    }

    @GetMapping(value = "/tour/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDTO<List<RankWeatherShortMainDTO>> getRankWeatherData() {
        ResultDTO<List<RankWeatherShortMainDTO>> list = tourApiService.getRankWeatherShortMainData();
        return list;
    }


}
