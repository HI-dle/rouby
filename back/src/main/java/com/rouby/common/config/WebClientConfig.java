package com.rouby.common.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
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
  WebClient fcmClient(
      @Value("${fcm.webClient.base-url:https://fcm.googleapis.com}") String baseUrl,
      @Value("${fcm.webClient.pool.max-connections:400}") int maxConns,
      @Value("${fcm.webClient.pool.pending-acquire-max:1000}") int pendingAcquireMax) {

    ConnectionProvider provider = ConnectionProvider.builder("fcm-pool")
        .maxConnections(maxConns)
        .pendingAcquireMaxCount(pendingAcquireMax)
        .pendingAcquireTimeout(Duration.ofSeconds(10))
        .maxIdleTime(Duration.ofMinutes(2))
        .maxLifeTime(Duration.ofMinutes(10))
        .evictInBackground(Duration.ofSeconds(30))
        .build();

    HttpClient httpClient = HttpClient.create(provider)
        .protocol(HttpProtocol.H2, HttpProtocol.HTTP11)
        .secure()
        .compress(true)
        .keepAlive(true)
        .responseTimeout(Duration.ofSeconds(10))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .baseUrl(baseUrl)
        .build();
  }
}
