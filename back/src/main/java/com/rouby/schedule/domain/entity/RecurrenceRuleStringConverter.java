package com.rouby.schedule.domain.entity;

import com.rouby.schedule.domain.vo.RecurrenceRule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RecurrenceRuleStringConverter implements AttributeConverter<RecurrenceRule, String> {

  @Override
  public String convertToDatabaseColumn(RecurrenceRule attribute) {
    if (attribute == null) return null;
    return attribute.toString();
  }

  @Override
  public RecurrenceRule convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) return null;
    return RecurrenceRule.from(dbData);
  }
}
