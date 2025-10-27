package com.rouby.assistant.feedback.application.service;

import com.rouby.assistant.feedback.application.dto.FeedbackInfoForNewFeedback;
import com.rouby.assistant.feedback.application.dto.GetDailyFeedbacksInfo;
import com.rouby.assistant.feedback.domain.entity.Feedback;
import com.rouby.assistant.feedback.domain.entity.enums.Status;
import com.rouby.assistant.feedback.domain.repository.FeedbackRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackReadService {

  private final FeedbackRepository feedbackRepository;

  public FeedbackInfoForNewFeedback getRecentFeedbackInfoWithin1W(Long userId) {

    var currentAt = LocalDate.now().atStartOfDay();
    Feedback recentFeedback = feedbackRepository.findTop1ByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            userId, currentAt.minusDays(7), currentAt)
        .orElse(null);
    return FeedbackInfoForNewFeedback.from(recentFeedback);
  }

  public GetDailyFeedbacksInfo getDailyFeedbacks(Long userId, LocalDate date) {

    return GetDailyFeedbacksInfo.from(
        feedbackRepository.findByUserIdAndFeedbackDateAndStatusAndDeletedAtIsNull(
            userId, date, Status.COMPLETED));
  }
}
