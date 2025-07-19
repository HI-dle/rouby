package com.rouby.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Embeddable
public class HealthStatusKeywords implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Set<String> healthStatusKeywords;

  public static HealthStatusKeywords empty(){
    return new HealthStatusKeywords(Collections.unmodifiableSet(new LinkedHashSet<>()));
  }

  public static HealthStatusKeywords of(Set<String> healthStatusKeywords){
    if(healthStatusKeywords != null && healthStatusKeywords.size() > 10) throw new IllegalArgumentException("healthStatusKeywords size 는 최대 10 입니다.");
    return new HealthStatusKeywords(Collections.unmodifiableSet(healthStatusKeywords));
  }

  private HealthStatusKeywords(Set<String> healthStatusKeywords) {
    this.healthStatusKeywords = healthStatusKeywords;
  }

  protected HealthStatusKeywords() {
  }

}
