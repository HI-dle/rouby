package com.rouby.batch.notification;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
public class NotificationQuartzJob implements Job {

  private final NotificationDispatcher dispatcher;

  @Override
  public void execute(JobExecutionContext ctx) {
    dispatcher.dispatch(ctx.getScheduledFireTime().toInstant());
  }
}
