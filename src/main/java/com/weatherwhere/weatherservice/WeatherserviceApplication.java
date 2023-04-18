package com.weatherwhere.weatherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
// JPA의 변화를 감시하는 어노테이션
@EnableJpaAuditing
@EnableCaching
@EnableBatchProcessing
public class WeatherserviceApplication {

    public static void main(String[] args) throws IOException {

        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> env = objectMapper.readValue(new File(rootPath + "/env.json"), Map.class);

        for (Map.Entry<String, String> entry : env.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
            //System.out.println("환경변수 테스트"+env);
        }
        SpringApplication.run(WeatherserviceApplication.class, args);

    }

    @Configuration
    public class WebConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("*")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                            .allowCredentials(true)
                            .maxAge(3600);
                }
            };
        }
    }
}
