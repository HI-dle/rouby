package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.OverrideType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 31.
 */
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OverrideInfo {

  @Enumerated(EnumType.STRING)
  private OverrideType overrideType;

  private LocalDate overrideDate;

  @Builder
  private OverrideInfo(OverrideType overrideType, LocalDate overrideDate) {

    this.overrideType = overrideType;
    this.overrideDate = overrideDate;
  }
}
