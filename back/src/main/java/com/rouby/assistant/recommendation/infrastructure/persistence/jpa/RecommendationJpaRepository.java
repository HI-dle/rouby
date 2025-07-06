package com.rouby.assistant.recommendation.infrastructure.persistence.jpa;

import com.rouby.assistant.recommendation.domain.Recommendation;
import com.rouby.assistant.recommendation.domain.repository.RecommendationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
public interface RecommendationJpaRepository extends JpaRepository<Recommendation, Long>,
    RecommendationRepository,RecommendationJpaRepositoryCustom {}
