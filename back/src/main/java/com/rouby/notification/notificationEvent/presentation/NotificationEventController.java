package com.rouby.notification.notificationEvent.presentation;

import com.rouby.notification.notificationEvent.application.usecase.CreateNotificationEventUsecase;
import com.rouby.notification.notificationEvent.presentation.dto.CreateNotificationEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/notification-events")
@RequiredArgsConstructor
public class NotificationEventController {

  private final CreateNotificationEventUsecase createNotificationEventUsecase;

  @PostMapping
  public ResponseEntity<Void> test(@RequestBody CreateNotificationEventRequest request) {
    createNotificationEventUsecase.sendNotification(request.toCommand());
    return ResponseEntity.noContent().build();
  }
}
