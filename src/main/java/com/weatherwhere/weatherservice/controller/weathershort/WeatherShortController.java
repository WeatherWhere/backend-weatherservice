package com.weatherwhere.weatherservice.controller.weathershort;

import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortSubDTO;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainApiService;

import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherShortController {

    private final WeatherShortMainService weatherShortMainService;
    private final WeatherShortMainApiService weatherShortMainApiService;


    //getmapping에는 requestbody 사용 x
    @GetMapping("/forecast/short")
    public String weatherShortMainEntityList(@ModelAttribute WeatherShortRequestDTO weatherShortRequestDTO) throws Exception {
/*        String nx = "69";
        String ny = "100";
        String baseDate = "20230323";
        String baseTime = "1700";*/
        try {
            return weatherShortMainService.saveWeatherShortEntity(weatherShortRequestDTO);

        } catch (Exception e) {
            throw new Exception(e);

        }
    }

    //단기예보 메인 api
    @GetMapping("/forecast/short/main")
    public ResponseEntity<List<WeatherShortMainDTO>> getWeatherShortMainResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) {
        try {
            List<WeatherShortMainDTO> mainDTOList = weatherShortMainApiService.getWeatherShortMainData(weatherShortMainApiRequestDTO);
            return new ResponseEntity<>(mainDTOList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //단기예보 서브 api
    @GetMapping("/forecast/short/sub")
    public ResponseEntity<List<WeatherShortSubDTO>> getWeatherShortSubResponse(@ModelAttribute WeatherShortMainApiRequestDTO weatherShortMainApiRequestDTO) {
        try {
            List<WeatherShortSubDTO> subDTOList = weatherShortMainApiService.getWeatherShortSubData(weatherShortMainApiRequestDTO);
            return new ResponseEntity<>(subDTOList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
