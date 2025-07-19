package com.rouby.common.props;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "setting")
public class SettingProperties {

  private final CommunicationToneProperties communicationToneProperties;


  @Getter
  @RequiredArgsConstructor
  public static class CommunicationToneProperties {

    private final int maxSize;
    private final String defaultTone;

  }
}
