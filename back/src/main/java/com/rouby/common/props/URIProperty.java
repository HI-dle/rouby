package com.rouby.common.props;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class URIProperty {

  private final String baseUrl;

  public URIProperty(String baseUrl) {
    this.baseUrl = baseUrl;

  }

  public String generateResetPasswordLink(String token) {
    return baseUrl + "/api/v1/users/password/reset/token?token=" + token;
  }

}
