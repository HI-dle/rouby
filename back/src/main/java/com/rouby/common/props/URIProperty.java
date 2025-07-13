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

  public String generateResetPasswordLink(String email, String token) {
   return baseUrl + "/api/v1/users/password/reset/token?email=" + email + "&token=" + token;
  }

}
