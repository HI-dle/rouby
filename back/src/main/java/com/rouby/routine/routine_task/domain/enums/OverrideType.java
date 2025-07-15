package com.rouby.routine.routine_task.domain.enums;

import lombok.Getter;

@Getter
public enum OverrideType {
  MODIFIED("수정됨"),
  CANCELLED("취소됨");

  private final String desc;

  OverrideType(String description) {
    this.desc = description;
  }

}

