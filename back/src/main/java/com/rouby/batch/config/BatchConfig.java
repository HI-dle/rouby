package com.rouby.batch.config;

import com.rouby.batch.job.briefing.dto.BriefingAggregate;
import com.rouby.batch.job.briefing.dto.UserBriefingInfo;
import com.rouby.batch.job.briefing.step.BriefingProcessor;
import com.rouby.batch.job.briefing.step.BriefingReader;
import com.rouby.batch.job.briefing.step.BriefingWriter;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

  private final BriefingReader briefingReader;
  private final BriefingProcessor briefingProcessor;
  private final BriefingWriter briefingWriter;

  @Bean
  public Job briefingJob(JobRepository jobRepository, Step briefingStep) {
    return new JobBuilder("briefingJob", jobRepository)
        .start(briefingStep)
        .incrementer(new RunIdIncrementer())
        .build();
  }

  @Bean
  public Step briefingStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("briefingStep", jobRepository)
        .<UserBriefingInfo, Future<BriefingAggregate>>chunk(20, transactionManager)
        .reader(briefingReader)
        .processor(asyncItemProcessor())
        .writer(asyncItemWriter())
        .build();
  }

  @Bean
  public AsyncItemProcessor<UserBriefingInfo, BriefingAggregate> asyncItemProcessor() {
    AsyncItemProcessor<UserBriefingInfo, BriefingAggregate> processor = new AsyncItemProcessor<>();
    processor.setDelegate(briefingProcessor);
    processor.setTaskExecutor(taskExecutor());
    return processor;
  }

  @Bean
  public AsyncItemWriter<BriefingAggregate> asyncItemWriter() {
    AsyncItemWriter<BriefingAggregate> writer = new AsyncItemWriter<>();
    writer.setDelegate(briefingWriter);
    return writer;
  }

  @Bean
  public TaskExecutor taskExecutor() {
    SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
    executor.setConcurrencyLimit(20);
    return executor;
  }
}
