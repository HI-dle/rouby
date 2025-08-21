package com.rouby.user.device.domain.entity.vo;

import com.rouby.user.device.domain.entity.enums.AppType;
import com.rouby.user.device.domain.entity.enums.DeviceType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class DeviceInfo {

  @Column(length = 50, nullable = false)
  @Enumerated(EnumType.STRING)
  private AppType appType;

  @Column(length = 50)
  private String appVersion;

  @Column(length = 50)
  @Enumerated(EnumType.STRING)
  private DeviceType deviceType;

  private String os;

  private String browser;

  private String userAgent;

  @Builder
  private DeviceInfo(AppType appType, String appVersion, DeviceType deviceType,
      String os, String browser, String userAgent) {

    this.appType = appType;
    this.appVersion = appVersion;
    this.deviceType = deviceType;
    this.os = os;
    this.browser = browser;
    this.userAgent = userAgent;
  }
}
