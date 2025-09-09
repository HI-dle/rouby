package com.rouby.batch;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class QuartzBriefingJob implements org.quartz.Job {

  private final JobLauncher jobLauncher;
  private final Job briefingJob;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addString("targetTime", LocalTime.now().minusHours(1).toString())
          .addLocalDate("today", LocalDate.now())
          .addLong("timestamp", System.currentTimeMillis())
          .toJobParameters();

      jobLauncher.run(briefingJob, jobParameters);

      log.info("✅ Briefing job completed by Quartz");
    } catch (Exception e) {
      log.error("❌ Briefing job failed", e);
      throw new JobExecutionException(e);
    }
  }

}
