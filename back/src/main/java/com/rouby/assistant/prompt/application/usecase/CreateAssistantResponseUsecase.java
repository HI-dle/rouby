package com.rouby.assistant.prompt.application.usecase;

import com.rouby.assistant.prompt.application.client.AssistantClient;
import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.application.service.PromptReadService;
import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import com.rouby.assistant.prompt.domain.info.AssistantResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateAssistantResponseUsecase {

  private final PromptReadService promptReadService;
  private final AssistantClient assistantClient;

  public <R> R createAssistantResponse(
      String promptType, int version, double temperature,
      Map<String, Object> model, Class<R> responseClazz) {

    PromptInfo promptInfo = promptReadService.findByPromptTypeAndVersion(
        PromptType.parse(promptType), version);

    AssistantResponse<R> response = assistantClient.generateResponseFromPrompt(
        temperature,
        promptInfo.systemMessage(),
        promptInfo.userMessage(),
        model,
        responseClazz
    );
    return response.result();
  }
}
