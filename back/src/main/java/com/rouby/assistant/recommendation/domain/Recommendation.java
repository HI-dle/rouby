package com.rouby.assistant.recommendation.domain;

import com.rouby.assistant.briefing.domain.vo.BriefingContent;
import com.rouby.assistant.recommendation.domain.vo.RecommendationContent;
import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Recommendation extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long promptTemplateId;

  @Embedded
  private RecommendationContent recommendationContent;

  @Builder
  private Recommendation(Long id, Long userId, Long promptTemplateId,
      String prompt, String recommendationResponse) {
    this.id = id;
    this.userId = userId;
    this.promptTemplateId = promptTemplateId;
    this.recommendationContent = RecommendationContent.of(prompt, recommendationResponse);
  }
}
