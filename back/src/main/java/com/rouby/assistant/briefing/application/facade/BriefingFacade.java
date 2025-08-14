package com.rouby.assistant.briefing.application.facade;

import static com.rouby.assistant.prompt.domain.enums.PromptType.BRIEFING;

import com.rouby.assistant.briefing.application.service.BriefingService;
import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.application.service.PromptReadService;
import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import com.rouby.schedule.application.service.ScheduleReadService;
import com.rouby.user.application.dto.info.UserInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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
  private final PromptReadService promptReadService;
  private final int promptVersion = 1;

  public Briefing createBriefing(UserInfo userInfo) {
    LocalDateTime today = LocalDateTime.now();
    String schedulesInfoJson = scheduleReadService.findSummarySchedulesJsonBy(
        GetScheduleQuery.builder()
            .userId(userInfo.id())
            .fromAt(today)
            .toAt(today.plusDays(7L))
            .build());

    PromptInfo promptInfo = promptReadService.findByPromptTypeAndVersion(BRIEFING, promptVersion);

    String prompt = promptReadService.generateBriefingPrompt(userInfo, schedulesInfoJson, promptInfo.promptTemplate()) ;

    String content = briefingService.sendPromptToAi(prompt);

    return Briefing.builder()
        .userId(userInfo.id())
        .prompt(prompt)
        .content(content)
        .build();
  }
}
