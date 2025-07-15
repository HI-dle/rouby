package com.rouby.common.props;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class URIProperty {

  private final String frontUrl;

  public URIProperty(String frontUrl) {
    this.frontUrl = frontUrl;

  }

  public String generateResetPasswordLink(String email, String token) {
   return frontUrl + "/auth/password/reset/token?email=" + email + "&token=" + token;
  }

}
