package com.example.KaneStream.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    public WebClient webClientLiveKit(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7880")
                .defaultHeader("Content-Type", "application/protobuf")
                .build();
    }
}
