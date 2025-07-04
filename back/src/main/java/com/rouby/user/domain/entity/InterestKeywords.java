package com.rouby.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Embeddable
public class InterestKeywords implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<String> interestKeywords;

  public static InterestKeywords empty(){
    return new InterestKeywords(new ArrayList<>());
  }

  public static InterestKeywords of(List<String> keywords) {
    return new InterestKeywords(keywords);
  }

  private InterestKeywords(List<String> interestKeywords) {
    this.interestKeywords = new ArrayList<>(interestKeywords);
  }

  protected InterestKeywords() {
  }
}
