package com.rouby.batch.notification;

import org.quartz.JobExecutionContext;

public interface NotificationDispatcher {

  void dispatch(JobExecutionContext ctx);
}
