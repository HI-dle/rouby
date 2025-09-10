package com.rouby.notification.notificationEvent.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode
public class NotificationMessage implements Serializable {

  @Column(length = 500, nullable = false)
  private String title;

  @Column(length = 1000, nullable = false)
  private String body;

  @Column(length = 1000)
  private String url;

  protected NotificationMessage() {}

  @Builder
  private NotificationMessage(String title, String body, String url) {
    this.title = title;
    this.body = body;
    this.url = url;
  }

  public NotificationMessage withUrl(String url) {
    return NotificationMessage.builder()
        .title(this.title)
        .body(this.body)
        .url(url)
        .build();
  }
}
