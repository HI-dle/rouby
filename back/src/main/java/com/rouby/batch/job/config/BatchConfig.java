package com.rouby.batch.job.config;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.batch.job.step.BriefingProcessor;
import com.rouby.batch.job.step.BriefingReader;
import com.rouby.batch.job.step.BriefingWriter;
import com.rouby.user.user.application.dto.info.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final BriefingReader briefingReader;
  private final BriefingProcessor briefingProcessor;
  private final BriefingWriter briefingWriter;

  @Bean
  public Job briefingJob(JobRepository jobRepository, Step briefingStep) {
    return new JobBuilder("briefingJob", jobRepository)
        .start(briefingStep)
        .build();
  }

  @Bean
  public Step briefingStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("briefingStep", jobRepository)
        .<UserInfo, Briefing>chunk(10, transactionManager)
        .reader(briefingReader)
        .processor(briefingProcessor::process)
        .writer(briefingWriter)
        .build();
  }
}
