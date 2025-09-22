package com.rouby.batch.config;

import com.rouby.batch.briefing.BriefingQuartzJob;
import java.util.TimeZone;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

  @Value("${app.quartz.briefingJob.cron}")
  private String briefingCron;

  @Value("${app.quartz.briefingJob.time-zone}")
  private String briefingTimeZone;

  @Bean
  public JobDetail briefingJobDetail() {
    return JobBuilder.newJob(BriefingQuartzJob.class)
        .withIdentity("briefingJob")
        .storeDurably()
        .build();
  }

  @Bean
  public Trigger briefingJobTrigger() {
    return TriggerBuilder.newTrigger()
        .forJob(briefingJobDetail())
        .withIdentity("briefingJobTrigger")
        .withSchedule(
            CronScheduleBuilder.cronSchedule(briefingCron)
                .inTimeZone(TimeZone.getTimeZone(briefingTimeZone))
                .withMisfireHandlingInstructionIgnoreMisfires()
        )
        .build();
  }
}
