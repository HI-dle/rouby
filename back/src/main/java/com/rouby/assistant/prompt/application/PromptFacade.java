package com.rouby.assistant.prompt.application;

import com.rouby.assistant.prompt.application.command.CreatePromptCommand;
import com.rouby.assistant.prompt.application.service.PromptWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptFacade {
  private final PromptWriteService promptWriteService;

  public void createPrompt(CreatePromptCommand command) {
    promptWriteService.createPrompt(command);
  }

}
