package com.rouby.common.config;

import com.rouby.assistant.prompt.infrastructure.exception.AssistantInfraException;
import com.rouby.common.exception.RateLimitException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.core.functions.Either.Left;
import io.github.resilience4j.core.registry.InMemoryRegistryStore;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.time.Duration;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Resilience4jRetryConfig {

  public RetryConfig assistantConfig() {

    return RetryConfig.custom()
        .maxAttempts(3)
        .ignoreExceptions(AssistantInfraException.class)
        .intervalBiFunction((attempt, ex) -> {
          Throwable t = null;
          if (ex instanceof Left l) t = (Throwable) l.getLeft();
          else if (ex instanceof Throwable th) t = th;

          if (t instanceof RateLimitException r
              && r.getRetryAfterSeconds() > 0 && r.getRetryAfterSeconds() <= 5) {
            return r.getRetryAfterSeconds() * 1000L;
          }

          int currentAttempt = ((Number) attempt).intValue();
          return IntervalFunction.ofExponentialBackoff(
              Duration.ofMillis(500), 2.0d, Duration.ofSeconds(5)
          ).apply(currentAttempt);
        })
        .build();
  }

  @Bean
  public RetryRegistry retryRegistry() {

    Set<Retry> allRetries = RetryRegistry.ofDefaults().getAllRetries();
    InMemoryRegistryStore<Retry> retryRegistryStore = new InMemoryRegistryStore<>();
    for (Retry retry : allRetries) {
      retryRegistryStore.putIfAbsent(retry.getName(), retry);
    }

    retryRegistryStore.putIfAbsent("assistant", Retry.of("assistant", assistantConfig()));

    return RetryRegistry.custom()
        .withRegistryStore(retryRegistryStore)
        .build();
  }
}
