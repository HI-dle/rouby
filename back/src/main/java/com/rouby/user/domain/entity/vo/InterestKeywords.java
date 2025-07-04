package com.rouby.user.domain.entity.vo;

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

  protected InterestKeywords() {
    this.interestKeywords = new ArrayList<>();
  }

  private InterestKeywords(List<String> interestKeywords) {
    this.interestKeywords = new ArrayList<>(interestKeywords);
  }

  public static InterestKeywords of(List<String> keywords) {
    return new InterestKeywords(keywords);
  }
}
