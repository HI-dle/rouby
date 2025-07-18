package com.rouby.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
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
    return new HealthStatusKeywords(new LinkedHashSet<>());
  }

  private HealthStatusKeywords(Set<String> healthStatusKeywords) {
    this.healthStatusKeywords = new LinkedHashSet<>(healthStatusKeywords);
  }

  protected HealthStatusKeywords() {
  }
}
