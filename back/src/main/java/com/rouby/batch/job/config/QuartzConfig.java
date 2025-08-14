package com.rouby.batch.job.config;

import com.rouby.batch.QuartzBriefingJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

  @Bean
  public JobDetail briefingJobDetail() {
    return JobBuilder.newJob(QuartzBriefingJob.class)
        .withIdentity("briefingJob")
        .storeDurably()
        .build();
  }

  @Bean
  public Trigger briefingJobTrigger() {
    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 4 * * * ?");

    return TriggerBuilder.newTrigger()
        .forJob(briefingJobDetail())
        .withIdentity("briefingJobTrigger")
        .withSchedule(scheduleBuilder)
        .build();
  }
}
