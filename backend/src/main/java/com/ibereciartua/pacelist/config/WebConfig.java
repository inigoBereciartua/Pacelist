package com.ibereciartua.pacelist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
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
                String currentDomain = variableUtils.getCurrentDomain();
                List<String> allowedOrigins = new ArrayList<>();
                allowedOrigins.add("https://" + currentDomain);
                if (currentDomain.equals("localhost")) {
                    allowedOrigins.add("http://localhost:8080");
                }

                registry.addMapping("/**")
                        .allowedOrigins(
                                allowedOrigins.toArray(new String[0])
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }
        };
    }
}
