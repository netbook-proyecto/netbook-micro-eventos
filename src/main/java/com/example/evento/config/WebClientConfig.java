package com.example.evento.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient eventowebClient() {
        return WebClient.builder().baseUrl("http://localhost:5010/evento.html").build();
    }

    @Bean
    public WebClient mensajeriawebClient() {
        return WebClient.builder().baseUrl("http://localhost:5008/mensajeria.html").build();
    }
}
