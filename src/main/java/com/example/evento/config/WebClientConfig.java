package com.example.evento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app.mensajeria.base-url}")
    private String mensajeriaBaseUrl;

    @Bean
    public WebClient mensajeriaWebClient() {
        return WebClient.builder().baseUrl(mensajeriaBaseUrl).build();
    }
}