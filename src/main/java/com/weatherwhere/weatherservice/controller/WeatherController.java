package com.weatherwhere.weatherservice.controller;

import com.weatherwhere.weatherservice.dto.TestDto;
import com.weatherwhere.weatherservice.dto.WeatherShortMainDto;
import com.weatherwhere.weatherservice.service.WeatherApiWebClientService;
import com.weatherwhere.weatherservice.service.WeatherShortApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherShortApiService weatherShortApiService;

    private final WeatherApiWebClientService weatherApiWebClientService;

    @GetMapping("/short-test")
    public WeatherShortMainDto weathertest(WeatherShortMainDto weatherShortMainDto) throws Exception{
        System.out.println("컨트롤러 서비스:"+weatherShortApiService.WeatherJsonParsing(weatherShortMainDto));
        return weatherShortApiService.WeatherJsonParsing(weatherShortMainDto);
    }

    @GetMapping("/test")
    public String test(){
        return "테스트";
    }


    @GetMapping("/webclient")
    public Flux<String> getTweetsNonBlocking() {
        System.out.println(weatherApiWebClientService.fluxService());
        return weatherApiWebClientService.fluxService();
    }




}
