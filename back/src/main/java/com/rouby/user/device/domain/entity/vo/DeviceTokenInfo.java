package com.rouby.user.device.domain.entity.vo;

import com.rouby.user.device.domain.entity.enums.TokenProviderType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceTokenInfo {

  @Column(length = 500, nullable = false)
  private String deviceToken;

  @Column(length = 50, nullable = false)
  @Enumerated(EnumType.STRING)
  private TokenProviderType tokenProvider;

  @Builder
  private DeviceTokenInfo(String deviceToken, TokenProviderType tokenProvider) {

    this.deviceToken = deviceToken;
    this.tokenProvider = tokenProvider;
  }
}
