package com.rouby.assistant.prompt.application.client;

import com.rouby.assistant.prompt.domain.info.AssistantResponse;
import java.util.Map;

public interface AssistantClient {

  <R> AssistantResponse<R> generateResponseFromPrompt(
      double temperature, String systemMsg, String userMsg,
      Map<String, Object> model, Class<R> responseClazz);
}
