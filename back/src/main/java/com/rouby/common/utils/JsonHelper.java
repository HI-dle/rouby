package com.rouby.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonHelper {

  private final ObjectMapper objectMapper;

  public String toJson(Object obj) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize object to JSON", e);
    }
  }
}
