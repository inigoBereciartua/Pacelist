package com.ibereciartua.pacelist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig {

    private final VariableUtils variableUtils;

    public WebConfig(VariableUtils variableUtils) {
        this.variableUtils = variableUtils;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                List<String> allowedOrigins = variableUtils.getAllowedOrigins();

                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);

                allowedOrigins.forEach(origin -> System.out.println("Allowed origin: " + origin));
            }
        };
    }
}
