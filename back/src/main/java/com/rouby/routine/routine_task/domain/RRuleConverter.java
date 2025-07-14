package com.rouby.routine.routine_task.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RRuleConverter implements AttributeConverter<RecurrenceRule, String> {

  @Override
  public String convertToDatabaseColumn(RecurrenceRule attribute) {
    if (attribute == null) return null;
    return attribute.toRRule(); // 객체 → RRULE 문자열
  }

  @Override
  public RecurrenceRule convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) return null;
    return RecurrenceRule.fromRRule(dbData); // 문자열 → 객체
  }
}

