package com.rouby.notification.notificationEvent.domain.repository;

import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import java.util.List;

public interface NotificationEventRepository {

  NotificationEvent save(NotificationEvent entity);

  <S extends NotificationEvent> List<S> saveAll(Iterable<S> entities);
}
