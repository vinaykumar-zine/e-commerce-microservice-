package com.vin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration  // Marks this class as a configuration class for Spring beans
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        // Creates and provides a reusable WebClient bean with default settings
        return WebClient.builder().build();
    }
}
