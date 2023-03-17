package com.weatherwhere.weatherservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class EnvLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        try {
            Map<String, Object> env = objectMapper.readValue(new ClassPathResource("env.json").getFile(), HashMap.class);
            propertySources.addFirst(new MapPropertySource("env", env));
        } catch (IOException e) {
            // Handle exception

        }
    }
}
