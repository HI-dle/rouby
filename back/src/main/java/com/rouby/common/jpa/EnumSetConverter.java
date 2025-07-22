package com.rouby.common.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Converter
public abstract class EnumSetConverter<T extends Enum<T>> implements AttributeConverter<Set<T>, String> {

  private final ObjectMapper objectMapper;
  private final Class<T> enumClass;

  protected EnumSetConverter(Class<T> enumClass, ObjectMapper objectMapper) {
    this.enumClass = enumClass;
    this.objectMapper = objectMapper;
  }

  @Override
  public String convertToDatabaseColumn(Set<T> attribute) {
    if (attribute == null || attribute.isEmpty()) return "[]";
    try {
      return objectMapper.writeValueAsString(attribute); // → ["MO","TU"]
    } catch (JsonProcessingException e) {
      throw new RuntimeException("데이터 저장에 실패하였습니다.", e);
    }
  }

  @Override
  public Set<T> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) return Collections.emptySet();
    try {
      Set<String> names = objectMapper.readValue(dbData, new TypeReference<Set<String>>() {});
      Set<T> result = new HashSet<>();
      for (String name : names) {
        result.add(Enum.valueOf(enumClass, name));
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException("데이터 변환에 실패하였습니다.", e);
    }
  }
}