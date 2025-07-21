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
public class CommunicationTone implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Set<String> roubyCommunicationTone;

  public static CommunicationTone empty(){
    return new CommunicationTone(new LinkedHashSet<>());
  }

  public static CommunicationTone of(Set<String> communicationTone, long maxSize) {
    if (communicationTone == null || communicationTone.isEmpty()) {
      return CommunicationTone.empty();
    }
    if(communicationTone.size() > maxSize){
      throw new IllegalArgumentException("말투 설정 최대 개수를 초과하였습니다.");
    }
    return new CommunicationTone(communicationTone);
  }

  private CommunicationTone(Set<String> communicationTone) {
    this.roubyCommunicationTone = new LinkedHashSet<>(communicationTone);
  }

  protected CommunicationTone() {
  }
}
