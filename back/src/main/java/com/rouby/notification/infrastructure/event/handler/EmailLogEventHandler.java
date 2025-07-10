package com.rouby.notification.infrastructure.event.handler;

import com.rouby.notification.application.service.EmailLogService;
import com.rouby.notification.application.service.event.SendEmailFailedEvent;
import com.rouby.notification.application.service.event.SendEmailSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailLogEventHandler {

  private final EmailLogService emailLogService;

  @EventListener
  public void sendEmailSuccess(SendEmailSuccessEvent event) {
    emailLogService.createSentLog(event.toCommand());
  }

  @EventListener
  public void sendEmailFailed(SendEmailFailedEvent event) {
    emailLogService.createFailedLog(event.toCommand());
  }
}
