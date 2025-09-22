package com.rouby.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

  @Bean
  WebClient fcmClient() {
    ConnectionProvider provider = ConnectionProvider.builder("fcm-pool")
        .maxConnections(400)
        .pendingAcquireMaxCount(1000)
        .build();

    var httpClient = HttpClient.create(provider)
        .protocol(HttpProtocol.H2, HttpProtocol.HTTP11)
        .secure()
        .compress(true)
        .keepAlive(true);

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .baseUrl("https://fcm.googleapis.com")
        .build();
  }
}
