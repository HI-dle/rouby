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
public class FeedbackKeyword {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb", nullable = false)
  private List<String> feedbackKeyword;

  private FeedbackKeyword(List<String> feedbackKeyword) {
    this.feedbackKeyword = new ArrayList<>(feedbackKeyword);
  }

  public static FeedbackKeyword of(List<String> feedbackKeyword) {
    return new FeedbackKeyword(feedbackKeyword);
  }

  public List<String> getFeedbackKeyword() {
    return List.copyOf(feedbackKeyword);
  }

}
