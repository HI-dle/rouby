package com.rouby.assistant.feedback.infrastructure.persistence.jpa;

import com.rouby.assistant.feedback.domain.Feedback;
import com.rouby.assistant.feedback.domain.repository.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
public interface FeedbackJpaRepository extends JpaRepository<Feedback, Long>,
    FeedbackRepository,FeedbackJpaRepositoryCustom {}
