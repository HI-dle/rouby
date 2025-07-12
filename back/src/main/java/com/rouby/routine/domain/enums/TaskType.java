package com.rouby.routine.domain.enums;

import lombok.Getter;

@Getter
public enum TaskType {
  COUNT("횟수"),
  MINUTES("시간"),
  CHECK("체크");

  private final String desc;

  TaskType(String description) {
    this.desc = description;
  }
}
