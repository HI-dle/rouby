package com.rouby.notification.notificationlog.presentation;

import com.rouby.notification.notificationlog.application.facade.NotificationLogFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification-logs")
@RequiredArgsConstructor
public class NotificationLogController {

  private final NotificationLogFacade notificationLogFacade;

}
