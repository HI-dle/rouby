package com.rouby.assistant.prompt.application.service;

import com.rouby.assistant.prompt.application.command.CreatePromptCommand;
import com.rouby.assistant.prompt.domain.PromptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptWriteService {
  private final PromptRepository promptRepository;

  public void createPrompt(CreatePromptCommand command) {
    promptRepository.save(command.toEntity());

  }
}
