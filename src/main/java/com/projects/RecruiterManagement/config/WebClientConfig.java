package com.projects.RecruiterManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .defaultHeader("apikey", "D1V90u47Fc2r3lUS0GFsCKdmTLyNCkID")
                .build();
    }
}
