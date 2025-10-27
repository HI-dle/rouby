package com.rouby.assistant.feedback.application.service;

import com.rouby.assistant.feedback.application.dto.CreateFeedbackCommand;
import com.rouby.assistant.feedback.application.dto.CreateFeedbackResult;
import com.rouby.assistant.feedback.application.exception.FeedbackErrorCode;
import com.rouby.assistant.feedback.application.exception.FeedbackException;
import com.rouby.assistant.feedback.domain.entity.Feedback;
import com.rouby.assistant.feedback.domain.entity.vo.FeedbackContent;
import com.rouby.assistant.feedback.domain.repository.FeedbackRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FeedbackWriteService {

  private final FeedbackRepository feedbackRepository;

  @Transactional
  public Long createFeedbackRequestWithinQuota(CreateFeedbackCommand command, Integer version) {

    var today = LocalDate.now();
    Integer usageCount = feedbackRepository.countDailyUsage(command.userId(), today).orElse(0);
    if (usageCount >= 3) throw FeedbackException.from(FeedbackErrorCode.EXCEEDED_DAILY_FEEDBACK_USAGE);

    Feedback feedback = command.toEntity(today, usageCount + 1, version);
    feedbackRepository.save(feedback);
    return feedback.getId();
  }

  @Transactional
  public void markSuccess(Long feedbackId, CreateFeedbackResult result) {
    Feedback feedback = feedbackRepository.findByIdAndDeletedAtIsNull(feedbackId)
        .orElseThrow(() -> FeedbackException.from(FeedbackErrorCode.INVALID_FEEDBACK_ID));
    feedback.markSuccessAndUpdate(
        FeedbackContent.of("", result.feedback()),
        result.feedbackKeywords(), result.userStatusKeywords());
  }

  @Transactional
  public void markFailure(Long feedbackId, Long userId) {
    feedbackRepository.markFailure(feedbackId, userId);
  }
}
