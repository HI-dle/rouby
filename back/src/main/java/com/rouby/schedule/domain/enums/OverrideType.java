package com.rouby.schedule.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OverrideType {

  MODIFIED("MODIFIED"),
  CANCELLED("CANCELLED")
  ;

  private final String name;
}
