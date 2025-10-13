package com.rouby.notification.notificationtemplate.application.dto;

import lombok.Builder;

@Builder
public record MessageInfo(
    String title,
    String body
) {

  public static MessageInfo of(String title, String body) {

    return MessageInfo.builder()
        .title(title)
        .body(body)
        .build();
  }
}
