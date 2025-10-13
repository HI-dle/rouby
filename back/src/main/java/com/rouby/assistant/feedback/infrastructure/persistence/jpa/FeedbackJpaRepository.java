package com.rouby.assistant.feedback.infrastructure.persistence.jpa;

import com.rouby.assistant.feedback.domain.entity.Feedback;
import com.rouby.assistant.feedback.domain.repository.FeedbackRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackJpaRepository extends
    JpaRepository<Feedback, Long>, FeedbackJpaRepositoryCustom, FeedbackRepository {

  Optional<Feedback> findByIdAndDeletedAtIsNull(Long feedbackId);

  Optional<Feedback> findTop1ByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
      Long userId, LocalDateTime fromAt, LocalDateTime toAt);

  @Query("""
    SELECT MAX(fb.slot) FROM Feedback fb
    WHERE fb.userId = :userId AND fb.feedbackDate = :today AND fb.status != 'FAILED'
    """)
  Optional<Integer> countDailyUsage(Long userId, LocalDate today);

  @Modifying
  @Query("""
    UPDATE Feedback fb
    SET fb.status = 'FAILED', fb.updatedAt = current_timestamp, fb.updatedBy = :userId
    WHERE fb.id = :feedbackId
    """)
  void markFailure(Long feedbackId, Long userId);
}
