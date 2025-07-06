package com.rouby.assistant.feedback.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackContent {

  @Column(nullable = false)
  private String prompt;

  @Column(nullable = false)
  private String content;

  private FeedbackContent(String prompt, String content) {
    this.prompt = prompt;
    this.content = content;
  }

  public static FeedbackContent of(String prompt, String content){
    return new FeedbackContent(prompt, content);
  }
}
