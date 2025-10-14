package com.rouby.assistant.prompt.application.service;

import com.rouby.assistant.prompt.application.command.CreatePromptCommand;
import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.domain.repository.PromptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromptWriteService {

  private final PromptRepository promptRepository;

  @Transactional
  public PromptInfo createPrompt(CreatePromptCommand command) {

    int version = promptRepository.findMaxVersionByPromptType(command.promptType()).orElse(0);
    return PromptInfo.from(promptRepository.save(command.toEntity(version + 1)));
  }
}
