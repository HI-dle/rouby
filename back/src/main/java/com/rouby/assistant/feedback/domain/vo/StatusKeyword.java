package com.rouby.assistant.feedback.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatusKeyword {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb", nullable = false)
  private List<String> statusKeyword;

  private StatusKeyword(List<String> statusKeyword) {
    this.statusKeyword = new ArrayList<>(statusKeyword);
  }

  public static StatusKeyword of(List<String> statusKeyword){
    return new StatusKeyword(statusKeyword);
  }

  //방어 복사(외부에서 컬렉션 수정 불가하게)
  public List<String> getStatusKeyword() {
    return List.copyOf(statusKeyword);
  }

}
