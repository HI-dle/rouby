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
public class ProfileKeywords implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Set<String> profileKeywords;

  public static ProfileKeywords empty(){
    return new ProfileKeywords(new LinkedHashSet<>());
  }

  boolean isEmpty() {
    return profileKeywords == null || profileKeywords.isEmpty();
  }

  private ProfileKeywords(Set<String> profileKeywords) {
    this.profileKeywords = new LinkedHashSet<>(profileKeywords);
  }

  protected ProfileKeywords() {
  }
}
