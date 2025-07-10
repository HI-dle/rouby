package com.rouby.notification.application.service.event;

import com.rouby.notification.application.service.CreateSentEmailLog;

public record SendEmailSuccessEvent(
    String address,
    String content
) {

  public static SendEmailSuccessEvent of(String address, String content) {
    return new SendEmailSuccessEvent(address, content);
  }

  public CreateSentEmailLog toCommand() {
    return new CreateSentEmailLog(address, content);
  }
}
