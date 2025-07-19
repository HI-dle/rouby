package com.rouby.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;

@Getter
@Embeddable
public class HealthStatusKeywords implements Serializable {

  @Column(columnDefinition = "text")
  @Convert(converter = EncryptedStringSetConverter.class)
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
