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
public class CurrentStatusKeywords implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Set<String> currentStatusKeywords;

  public static CurrentStatusKeywords empty(){
    return new CurrentStatusKeywords(new LinkedHashSet<>());
  }

  private CurrentStatusKeywords(Set<String> currentStatusKeywords) {
    this.currentStatusKeywords = new LinkedHashSet<>(currentStatusKeywords);
  }

  protected CurrentStatusKeywords() {
  }
}
