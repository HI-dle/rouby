package com.rouby.common.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ApiErrorCode;
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
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw CustomException.from(ApiErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public Set<T> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) return Collections.emptySet();
    try {
      Set<String> names = objectMapper.readValue(dbData, new TypeReference<>() {});
      Set<T> result = new HashSet<>();
      for (String name : names) {
        result.add(Enum.valueOf(enumClass, name));
      }
      return result;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("유효하지 않은 코드 데이터가 존재합니다.", e);
    } catch (IOException e) {
      throw CustomException.from(ApiErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}