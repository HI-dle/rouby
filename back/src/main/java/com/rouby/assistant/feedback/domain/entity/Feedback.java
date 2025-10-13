package com.rouby.assistant.feedback.domain.entity;

import com.rouby.assistant.feedback.domain.entity.enums.Mood;
import com.rouby.assistant.feedback.domain.entity.enums.Status;
import com.rouby.assistant.feedback.domain.entity.vo.FeedbackContent;
import com.rouby.assistant.feedback.domain.entity.vo.FeedbackKeyword;
import com.rouby.assistant.feedback.domain.entity.vo.StatusKeyword;
import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private LocalDate feedbackDate;

  @Column(nullable = false)
  private Integer slot;

  @Column(nullable = false, length = 10)
  @Enumerated(value = EnumType.STRING)
  private Status status;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Mood mood;

  @Column(nullable = false, length = 1000)
  private String userInput;

  @Column(nullable = false)
  private Integer promptTemplateVersion;

  @Embedded
  private FeedbackContent feedbackContent;

  @Embedded
  private StatusKeyword userStatusKeyword;

  @Embedded
  private FeedbackKeyword feedbackKeyword;

  @Builder
  private Feedback(Long userId, Mood mood, String userInput, LocalDate feedbackDate, int slot,
      Integer promptTemplateVersion, String prompt, String content,
      StatusKeyword userStatusKeyword, FeedbackKeyword feedbackKeyword) {

    this.status = Status.PENDING;
    this.userId = userId;
    this.feedbackDate = feedbackDate;
    this.slot = slot;
    this.mood = mood;
    this.userInput = userInput;

    this.promptTemplateVersion = promptTemplateVersion;
    this.feedbackContent = FeedbackContent.of(prompt, content);

    this.userStatusKeyword = userStatusKeyword;
    this.feedbackKeyword = feedbackKeyword;
  }

  public void markSuccessAndUpdate(
      FeedbackContent content, List<String> feedbackKeyword, List<String> userStatusKeyword) {

    this.status = Status.COMPLETED;
    this.feedbackContent = content;
    this.userStatusKeyword = StatusKeyword.of(userStatusKeyword);
    this.feedbackKeyword = FeedbackKeyword.of(feedbackKeyword);
  }
}
