package com.rouby.common.config;

import com.rouby.common.props.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final CorsProperties corsProperties;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowCredentials(true)
        .allowedOriginPatterns(corsProperties.getAllowedOriginPatterns())
        .allowedMethods(corsProperties.getAllowedMethods())
        .allowedHeaders(corsProperties.getAllowedHeaders())
        .exposedHeaders("Location")
        //.exposedHeaders() // , "*"
        .maxAge(corsProperties.getMaxAgeSec());
  }
}