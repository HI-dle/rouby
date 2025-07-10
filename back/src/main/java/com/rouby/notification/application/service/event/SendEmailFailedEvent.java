package com.rouby.notification.application.service.event;

import com.rouby.notification.application.service.CreateFailedEmailLog;

public record SendEmailFailedEvent(
    String address,
    String content
) {

  public static SendEmailFailedEvent of(String address, String content) {
    return new SendEmailFailedEvent(address, content);
  }

  public CreateFailedEmailLog toCommand() {
    return new CreateFailedEmailLog(address, content);
  }
}
