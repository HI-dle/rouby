package com.rouby.user.domain.entity;

import com.rouby.common.jpa.EncryptedStringSetConverter;
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
public class ProfileKeywords implements Serializable {

  @Column(columnDefinition = "text")
  @Convert(converter = EncryptedStringSetConverter.class)
  private Set<String> profileKeywords;

  public static ProfileKeywords empty(){
    return new ProfileKeywords(Collections.unmodifiableSet(new LinkedHashSet<>()));
  }

  public static ProfileKeywords of(Set<String> profileKeywords){
    if(profileKeywords != null && profileKeywords.size() > 10) throw new IllegalArgumentException("profileKeywords size 는 최대 10 입니다.");
    return new ProfileKeywords(Collections.unmodifiableSet(profileKeywords));
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
