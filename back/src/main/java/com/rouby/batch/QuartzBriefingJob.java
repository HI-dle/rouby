package com.rouby.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class QuartzBriefingJob implements org.quartz.Job {

  private final JobLauncher jobLauncher;
  private final Job briefingJob;

  @Override
  public void execute(JobExecutionContext context) {
    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addLong("timestamp", System.currentTimeMillis())
          .toJobParameters();

      jobLauncher.run(briefingJob, jobParameters);

      log.info("✅ Briefing job completed by Quartz");
    } catch (Exception e) {
      log.error("❌ Briefing job failed", e);
    }
  }

}
