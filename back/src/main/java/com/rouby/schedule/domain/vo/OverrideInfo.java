package com.rouby.schedule.domain.vo;

import com.rouby.schedule.domain.enums.OverrideType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
