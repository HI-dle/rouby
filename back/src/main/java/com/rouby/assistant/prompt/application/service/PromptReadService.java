package com.rouby.assistant.prompt.application.service;

import static com.rouby.assistant.prompt.application.exception.PromptErrorCode.PROMPT_NOT_FOUNT;

import com.rouby.assistant.prompt.application.exception.PromptException;
import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.domain.PromptRepository;
import com.rouby.assistant.prompt.domain.enums.PromptType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptReadService {

  private final PromptRepository promptRepository;

  public PromptInfo findByPromptTypeAndVersion(PromptType promptType, Integer version) {
    return PromptInfo.of(promptRepository.findByPromptTypeAndVersion(promptType, version)
        .orElseThrow(() -> PromptException.from(PROMPT_NOT_FOUNT)));
  }
}
