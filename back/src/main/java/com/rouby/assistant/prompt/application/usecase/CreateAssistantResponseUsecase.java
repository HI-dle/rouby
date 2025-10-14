package com.rouby.assistant.prompt.application.usecase;

import com.rouby.assistant.prompt.application.client.AssistantClient;
import com.rouby.assistant.prompt.application.exception.PromptErrorCode;
import com.rouby.assistant.prompt.application.exception.PromptException;
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

    PromptInfo promptInfo;
    try {
      promptInfo = promptReadService.findByPromptTypeAndVersion(
          PromptType.parse(promptType), version);
    } catch (IllegalArgumentException e) {
      throw PromptException.from(PromptErrorCode.INVALID_PROMPT_TYPE);
    } catch (Exception e) {
      throw PromptException.from(PromptErrorCode.PROMPT_NOT_FOUND);
    }

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
