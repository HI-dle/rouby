package com.rouby.schedule.domain.entity;

import com.rouby.schedule.domain.vo.RecurrenceRule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RecurrenceRuleStringConverter implements AttributeConverter<RecurrenceRule, String> {

  @Override
  public String convertToDatabaseColumn(RecurrenceRule attribute) {
    return attribute.toString();
  }

  @Override
  public RecurrenceRule convertToEntityAttribute(String dbData) {
    return RecurrenceRule.from(dbData);
  }
}
