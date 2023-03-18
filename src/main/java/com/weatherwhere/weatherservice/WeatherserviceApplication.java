package com.weatherwhere.weatherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class WeatherserviceApplication {

	public static void main(String[] args) throws IOException {

		String rootPath = System.getProperty("user.dir");
		System.out.println(rootPath);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> env = objectMapper.readValue(new File(rootPath+"/env.json"), Map.class);

		for (Map.Entry<String, String> entry : env.entrySet()) {
			System.setProperty(entry.getKey(), entry.getValue());
			System.out.println("환경변수 테스트"+env);
		}


		SpringApplication.run(WeatherserviceApplication.class, args);




	}

}