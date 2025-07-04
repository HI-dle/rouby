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
public class CommunicationTone implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<String> roubyCommunicationTone;

  protected CommunicationTone() {
  }

  private CommunicationTone(List<String> communicationTone) {
    this.roubyCommunicationTone = new ArrayList<>(communicationTone);
  }

  public static CommunicationTone empty(){
    return new CommunicationTone(new ArrayList<>());
  }

  public static CommunicationTone of(List<String> communicationTone) {
    return new CommunicationTone(communicationTone);
  }
}
