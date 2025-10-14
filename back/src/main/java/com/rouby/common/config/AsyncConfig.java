package com.rouby.common.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

  @Bean(name = "llmExecutor")
  public ThreadPoolTaskExecutor llmExecutor() {
    ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
    ex.setThreadNamePrefix("llm-");
    ex.setCorePoolSize(4);
    ex.setMaxPoolSize(16);
    ex.setQueueCapacity(200);
    ex.setKeepAliveSeconds(60); // 임시 스레드가 idle 상태로 유지될 수 있는 최대 시간
    ex.setWaitForTasksToCompleteOnShutdown(true);
    ex.setAwaitTerminationSeconds(30);
    ex.setTaskDecorator(r -> r);
    ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    ex.initialize();
    return ex;
  }

  @Override
  public Executor getAsyncExecutor() {
    return null; // 디폴트는 기본 실행기
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

    return (ex, method, params) ->
        log.error("Async error at {}: {}", method.getName(), ex.getMessage(), ex);
  }
}
