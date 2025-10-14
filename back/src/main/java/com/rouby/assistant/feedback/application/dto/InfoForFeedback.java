package com.rouby.assistant.feedback.application.dto;

import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record InfoForFeedback(
    String userInput,
    String userMood,
    String nickname,
    Set<String> communicationTone,
    Set<String> profileKeywords,
    Set<String> healthStatusKeywords,
    List<String> recentStatusKeywords,
    List<String> recentFeedbackKeywords,
    SchedulesInfoForFeedback schedulesInfo,
    RoutineTasksInfoForFeedback routineTasksWithProgress
) {

  public static InfoForFeedback from(
      CreateFeedbackCommand command,
      UserInfoForFeedback userInfo,
      RoutineTasksInfoForFeedback routineTasksWithProgress,
      SchedulesInfoForFeedback schedulesInfo,
      FeedbackInfoForNewFeedback recentFeedbackInfo) {

    return InfoForFeedback.builder()
        .userInput(command.userInput())
        .userMood(command.userMood())
        .nickname(userInfo.nickname())
        .communicationTone(userInfo.communicationTone())
        .profileKeywords(userInfo.profileKeywords())
        .healthStatusKeywords(userInfo.healthStatusKeywords())
        .recentStatusKeywords(recentFeedbackInfo.statusKeyword())
        .recentFeedbackKeywords(recentFeedbackInfo.feedbackKeyword())
        .schedulesInfo(schedulesInfo)
        .routineTasksWithProgress(routineTasksWithProgress)
        .build();
  }
}
