package com.rouby.assistant.feedback.domain.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
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
  @Column(columnDefinition = "jsonb")
  private List<String> feedbackKeyword = new ArrayList<>();

  private FeedbackKeyword(List<String> feedbackKeyword) {

    if (feedbackKeyword == null || feedbackKeyword.isEmpty()) return;
    this.feedbackKeyword = new ArrayList<>(feedbackKeyword);
  }

  public static FeedbackKeyword of(List<String> feedbackKeyword) {
    return new FeedbackKeyword(feedbackKeyword);
  }

  public List<String> getFeedbackKeyword() {
    return this.feedbackKeyword == null ? Collections.emptyList() : List.copyOf(feedbackKeyword);
  }

}
