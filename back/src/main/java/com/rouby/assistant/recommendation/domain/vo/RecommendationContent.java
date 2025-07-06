package com.rouby.assistant.recommendation.domain.vo;

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
public class RecommendationContent {

  @Column(nullable = false, columnDefinition = "TEXT")
  private String prompt;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String recommendationResponse;

  private RecommendationContent(String prompt, String recommendationResponse) {
    this.prompt = prompt;
    this.recommendationResponse = recommendationResponse;
  }

  public static RecommendationContent of(String prompt, String recommendationResponse){
    return new RecommendationContent(prompt, recommendationResponse);
  }
}


