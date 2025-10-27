package com.rouby.assistant.feedback.application.usecase;

import com.rouby.assistant.feedback.application.dto.CreateFeedbackCommand;
import com.rouby.assistant.feedback.application.dto.CreateFeedbackResult;
import com.rouby.assistant.feedback.application.dto.FeedbackInfoForNewFeedback;
import com.rouby.assistant.feedback.application.dto.FeedbackNotiTargetUserInfo;
import com.rouby.assistant.feedback.application.dto.InfoForFeedback;
import com.rouby.assistant.feedback.application.dto.RoutineTasksInfoForFeedback;
import com.rouby.assistant.feedback.application.dto.SchedulesInfoForFeedback;
import com.rouby.assistant.feedback.application.dto.UserInfoForFeedback;
import com.rouby.assistant.feedback.application.exception.FeedbackErrorCode;
import com.rouby.assistant.feedback.application.exception.FeedbackException;
import com.rouby.assistant.feedback.application.port.outbound.AssistantGateway;
import com.rouby.assistant.feedback.application.port.outbound.NotificationEventGateway;
import com.rouby.assistant.feedback.application.port.outbound.RoutineTaskGateway;
import com.rouby.assistant.feedback.application.port.outbound.ScheduleGateway;
import com.rouby.assistant.feedback.application.port.outbound.UserGateway;
import com.rouby.assistant.feedback.application.service.FeedbackReadService;
import com.rouby.assistant.feedback.application.service.FeedbackWriteService;
import java.time.LocalDate;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateFeedbackUsecase {

  @Value("${prompt.feedback.version:1}")
  private int PROMPT_VERSION;
  @Value("${spring.ai.openai.chat.options.temperature:0.2}")
  private double TEMPERATURE;
  private static final String FEEDBACK_URL_PREFIX = "/feedback/daily/";

  private final FeedbackReadService feedbackReadService;
  private final FeedbackWriteService feedbackWriteService;

  private final AssistantGateway assistantGateway;
  private final UserGateway userGateway;
  private final RoutineTaskGateway routineTaskGateway;
  private final ScheduleGateway scheduleGateway;
  private final NotificationEventGateway notificationEventGateway;

  public void requestFeedback(CreateFeedbackCommand command) {

    Long feedbackId;
    try{
      feedbackId = feedbackWriteService.createFeedbackRequestWithinQuota(command, PROMPT_VERSION);
    } catch (DataIntegrityViolationException e) {
      String lowerCaseMessage = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
      if (lowerCaseMessage.contains("duplicate entry") || lowerCaseMessage.contains("unique constraint")) {
        if (lowerCaseMessage.contains("uq_feedback_user_daily_slot")) {
          throw FeedbackException.from(FeedbackErrorCode.CONCURRENT_REQUEST_FAILED);
        }
      }
      throw e;
    }

    var today = LocalDate.now();
    var toDate = today.plusDays(1);
    var fromDate = toDate.minusDays(7);

    UserInfoForFeedback userInfo;
    RoutineTasksInfoForFeedback routineTaskWithProgress;
    SchedulesInfoForFeedback schedulesInfo;
    FeedbackInfoForNewFeedback recentFeedbackInfo;

    try {
      userInfo = userGateway.getUserInfoWithNotification(command.userId());
      routineTaskWithProgress = routineTaskGateway.getRoutineTaskWithProgress(
          command.userId(), fromDate, toDate);
      schedulesInfo = scheduleGateway.getSchedulesByCondition(
          command.userId(), fromDate.atStartOfDay(), toDate.atStartOfDay());
      recentFeedbackInfo =
          feedbackReadService.getRecentFeedbackInfoWithin1W(command.userId());
    } catch (Exception e) {
      onFailure(e, feedbackId, command.userId());
      throw e;
    }

    TEMPERATURE = 0.5;
    assistantGateway.requestDailyFeedbackAsync(
        PROMPT_VERSION,
        TEMPERATURE,
        InfoForFeedback.from(
            command, userInfo, routineTaskWithProgress, schedulesInfo, recentFeedbackInfo),
        CreateFeedbackResult.class
        )
        .thenAccept(result -> {
          onSuccess(result, feedbackId,
              FeedbackNotiTargetUserInfo.from(command.userId(), userInfo), today);
        })
        .whenComplete((result, t) -> {
          if (t != null) {
            onFailure(t, feedbackId, command.userId());
          }
        });
  }

  public void onSuccess(CreateFeedbackResult result,
      Long feedbackId, FeedbackNotiTargetUserInfo targetUserInfo, LocalDate feedbackDate) {

    try {
      feedbackWriteService.markSuccess(feedbackId, result);
    } catch (Exception e) {
      log.error("어시스턴트의 피드백 생성 성공 로직 수행 중 데이터베이스 업데이트 오류 발생으로 인한 실패 처리: {}", feedbackId, e);
      throw new CompletionException(e);
    }

    try {
      notificationEventGateway.createFeedbackNotificationEvent(
          targetUserInfo, FEEDBACK_URL_PREFIX + feedbackDate.toString());
    } catch (Exception e) {
      log.error("어시스턴트의 피드백 생성 성공 로직 수행 중 알림 생성 오류 발생: {}", feedbackId, e);
    }
  }

  public void onFailure(Throwable t, Long feedbackId, Long userId) {

    log.error("어시스턴트의 피드백 생성 중 오류 발생으로 인한 실패 처리: {}", t.getMessage(), t);
    feedbackWriteService.markFailure(feedbackId, userId);
  }
}
