package com.rouby.batch.notification;

import java.time.Instant;

public interface NotificationDispatcher {

  void dispatch(Instant sched);
}
