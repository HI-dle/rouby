package com.rouby.batch.job.step;

import com.rouby.assistant.briefing.application.facade.BriefingFacade;
import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.user.user.application.dto.info.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BriefingProcessor {

  private final BriefingFacade briefingFacade;

  public Briefing process(UserInfo user) {
    return briefingFacade.createBriefing(user);
  }
}
