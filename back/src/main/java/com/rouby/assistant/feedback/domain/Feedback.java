package com.rouby.assistant.feedback.domain;

import com.rouby.assistant.feedback.domain.enums.Mood;
import com.rouby.assistant.feedback.domain.vo.FeedbackContent;
import com.rouby.assistant.feedback.domain.vo.FeedbackKeyword;
import com.rouby.assistant.feedback.domain.vo.StatusKeyword;
import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long promptTemplateId;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Mood mood;

  @Column(nullable = false)
  private String userInput;

  @Embedded
  private FeedbackContent feedbackContent;

  @Embedded
  private StatusKeyword statusKeyword;

  @Embedded
  private FeedbackKeyword feedbackKeyword;

  @Builder
  public Feedback(Long id, Long userId, Long promptTemplateId, Mood mood, String userInput,
      String prompt, String content, List<String> statusKeyword,
      List<String> feedbackKeyword) {
    this.id = id;
    this.userId = userId;
    this.promptTemplateId = promptTemplateId;
    this.mood = mood;
    this.userInput = userInput;
    this.feedbackContent = FeedbackContent.of(prompt, content);
    this.statusKeyword = StatusKeyword.of(statusKeyword);
    this.feedbackKeyword = FeedbackKeyword.of(feedbackKeyword);
  }
}
