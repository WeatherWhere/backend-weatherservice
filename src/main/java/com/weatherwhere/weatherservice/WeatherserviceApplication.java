package com.weatherwhere.weatherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
// JPA의 변화를 감시하는 어노테이션
@EnableJpaAuditing
public class WeatherserviceApplication {
    public static void main(String[] args) throws IOException {

<<<<<<< HEAD
	public static void main(String[] args) throws IOException {

		String rootPath = System.getProperty("user.dir");
		System.out.println(rootPath);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> env = objectMapper.readValue(new File(rootPath+"/env.json"), Map.class);

		for (Map.Entry<String, String> entry : env.entrySet()) {
			System.setProperty(entry.getKey(), entry.getValue());
			//System.out.println("환경변수 테스트"+env);
		}


		SpringApplication.run(WeatherserviceApplication.class, args);




	}

=======
        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> env = objectMapper.readValue(new File(rootPath + "/env.json"), Map.class);

        for (Map.Entry<String, String> entry : env.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
            System.out.println("환경변수 테스트" + env);
        }


        SpringApplication.run(WeatherserviceApplication.class, args);


    }
>>>>>>> a7d1c1e1f0b1d3e4ce96d89b0a4223b212d1ffab
}