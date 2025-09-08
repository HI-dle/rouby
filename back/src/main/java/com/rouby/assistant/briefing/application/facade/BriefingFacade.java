package com.rouby.assistant.briefing.application.facade;

import static com.rouby.assistant.prompt.domain.enums.PromptType.BRIEFING;

import com.rouby.assistant.briefing.application.dto.info.CreatedBriefingResult;
import com.rouby.assistant.briefing.application.service.BriefingService;
import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.application.service.PromptReadService;
import com.rouby.batch.job.briefing.dto.UserBriefingInfo;
import com.rouby.routine.routine_task.application.dto.command.GetRoutineTaskCommand;
import com.rouby.routine.routine_task.application.service.RoutineTaskReadService;
import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import com.rouby.schedule.application.service.ScheduleReadService;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 07.
 */
@Service
@RequiredArgsConstructor
public class BriefingFacade {

  private final BriefingService briefingService;
  private final ScheduleReadService scheduleReadService;
  private final RoutineTaskReadService routineTaskReadService;
  private final PromptReadService promptReadService;

  @Value("${prompt.version}")
  private int PROMPT_VERSION = 1;

  public CreatedBriefingResult createBriefingForBatch(UserBriefingInfo userBriefingInfo) {
    LocalDate today = LocalDate.now();
    String schedulesInfoJson = scheduleReadService.findSummarySchedulesJsonBy(
        GetScheduleQuery.builder()
            .userId(userBriefingInfo.userInfo().id())
            .fromAt(today.atStartOfDay())
            .toAt(today.plusDays(7).atTime(LocalTime.MAX))
            .build());

    String routineTaskInfoJson = routineTaskReadService.findSummaryRoutineTaskJsonBy(
        GetRoutineTaskCommand.builder()
            .userId(userBriefingInfo.userInfo().id())
            .fromDate(today)
            .toDate(today.plusDays(7L))
            .build());

    PromptInfo promptInfo = promptReadService.findByPromptTypeAndVersion(BRIEFING, PROMPT_VERSION);

    String prompt = promptReadService.generateBriefingPrompt(
        userBriefingInfo.userInfo(), schedulesInfoJson, routineTaskInfoJson,
        promptInfo.promptTemplate());

    String content = briefingService.sendPromptToAi(prompt);

    return CreatedBriefingResult.of(
        userBriefingInfo.userInfo().id(),
        prompt,
        promptInfo.id(),
        content
    );
  }
}
