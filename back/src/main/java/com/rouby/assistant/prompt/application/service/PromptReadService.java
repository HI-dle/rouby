package com.rouby.assistant.prompt.application.service;

import static com.rouby.assistant.prompt.application.exception.PromptErrorCode.PROMPT_NOT_FOUND;

import com.rouby.assistant.prompt.application.exception.PromptException;
import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.domain.PromptRepository;
import com.rouby.assistant.prompt.domain.enums.PromptType;
import com.rouby.user.user.application.dto.info.UserInfo;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptReadService {

  private final PromptRepository promptRepository;

  public PromptInfo findByPromptTypeAndVersion(PromptType promptType, Integer version) {
    return PromptInfo.of(promptRepository.findByPromptTypeAndVersion(promptType, version)
        .orElseThrow(() -> PromptException.from(PROMPT_NOT_FOUND)));
  }

  public String generateBriefingPrompt(UserInfo user, String schedulesJson, String routineJson,
      String promptTemplate) {
    String userName = user.nickname();
    String tone = String.join(", ", user.communicationTone().tones());
    String profile = String.join(", ", user.profileKeywords().keywords());
    String health = String.join(", ", user.healthStatusKeywords().keywords());

    return String.format(promptTemplate, LocalDate.now(), userName, tone, profile, health,
        schedulesJson, routineJson);
  }
}
