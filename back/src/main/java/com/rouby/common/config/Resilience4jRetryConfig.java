package com.rouby.common.config;

import com.rouby.assistant.prompt.infrastructure.exception.AssistantInfraException;
import com.rouby.common.exception.RateLimitException;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.core.functions.Either;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Resilience4jRetryConfig {

  @Bean
  RetryConfigCustomizer assistantConfigCustomizer() {

    return RetryConfigCustomizer.of("assistant", builder -> builder
        .maxAttempts(3)
        .ignoreExceptions(AssistantInfraException.class)
        .intervalBiFunction((attempt, ex) -> {
          Throwable t = null;
          if (ex instanceof Either.Left l) t = (Throwable) l.getLeft();
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
    );
  }
}
