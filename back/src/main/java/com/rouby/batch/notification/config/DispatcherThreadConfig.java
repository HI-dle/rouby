package com.rouby.batch.notification.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class DispatcherThreadConfig {

  @Bean("notificationSendExecutor")
  public ThreadPoolTaskExecutor notificationSendExecutor() {

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(1000);
    executor.setThreadNamePrefix("notiSendEx-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    executor.initialize();
    return executor;
  }

  @Bean("resultCallbackExecutor")
  public ThreadPoolTaskExecutor resultCallbackExecutor() {

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(400);
    executor.setThreadNamePrefix("notiCb-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
    executor.initialize();
    return executor;
  }

  @Bean("preciseTimer")
  public ScheduledExecutorService preciseTimer() {

    return Executors.newScheduledThreadPool(2);
  }

  @Bean("flushScheduler")
  public ScheduledExecutorService flushScheduler() {

    return Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "flush"));
  }
}
