package com.weatherwhere.weatherservice;

import com.weatherwhere.weatherservice.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(EnvLoader.class)
public class WeatherserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherserviceApplication.class, args);
    }

}
