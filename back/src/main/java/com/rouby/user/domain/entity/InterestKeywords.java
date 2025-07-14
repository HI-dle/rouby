package com.rouby.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Embeddable
public class InterestKeywords implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Set<String> interestKeywords;

  public static InterestKeywords empty(){
    return new InterestKeywords(new HashSet<>());
  }

  public static InterestKeywords of(Set<String> keywords) {
    return new InterestKeywords(keywords);
  }

  private InterestKeywords(Set<String> interestKeywords) {
    this.interestKeywords = new HashSet<>(interestKeywords);
  }

  protected InterestKeywords() {
  }
}
