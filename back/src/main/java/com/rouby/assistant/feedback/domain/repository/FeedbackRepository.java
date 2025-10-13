package com.rouby.assistant.feedback.domain.repository;

import com.rouby.assistant.feedback.domain.entity.Feedback;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface FeedbackRepository {

  Optional<Feedback> findTop1ByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
      Long userId, LocalDateTime fromAt, LocalDateTime toAt);

  Optional<Integer> countDailyUsage(Long userId, LocalDate today);

  Feedback save(Feedback feedback);

  void markFailure(Long feedbackId, Long userId);

  Optional<Feedback> findByIdAndDeletedAtIsNull(Long feedbackId);
}
