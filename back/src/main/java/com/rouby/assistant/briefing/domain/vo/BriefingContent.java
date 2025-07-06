package com.rouby.assistant.briefing.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 * @author : hanjihoon
 */
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BriefingContent {
  @Column(nullable = false, columnDefinition = "TEXT")
  private String prompt;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  public BriefingContent(String prompt, String content) {
    this.prompt = prompt;
    this.content = content;
  }
}
