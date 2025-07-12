package com.rouby.common.props;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

  private final String[] allowedOriginPatterns;
  private final String[] allowedMethods;
  private final String[] allowedHeaders;
  private final long maxAgeSec;

  public CorsProperties(
      String[] allowedOriginPatterns,
      String[] allowedMethods,
      String[] allowedHeaders,
      long maxAgeSec) {
    this.allowedOriginPatterns = allowedOriginPatterns;
    this.allowedMethods = allowedMethods;
    this.allowedHeaders = allowedHeaders;
    this.maxAgeSec = maxAgeSec;
  }
}