package com.rouby.assistant.prompt.infrastructure.ai;

import com.rouby.assistant.prompt.application.client.AssistantClient;
import com.rouby.assistant.prompt.domain.info.AssistantResponse;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantErrorCode;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantException;
import com.rouby.common.utils.JsonHelper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeminiAssistantClient implements AssistantClient {

  private final ChatClient chatClient;
  private final JsonHelper jsonHelper;
  private final ResponseConverterManager responseConverterManager;

  @Retry(name = "assistant")
  @CircuitBreaker(name = "assistant", fallbackMethod = "fallback")
  @Override
  public <R> AssistantResponse<R> generateResponseFromPrompt(
      double temperature, String systemMsg, String userMsg,
      Map<String, Object> model, Class<R> responseClazz) {

    final Map<String, Object> jsonModel = model.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey, entry-> jsonHelper.toJson(entry.getValue())));
    BeanOutputConverter<R> converter = responseConverterManager.getConverter(responseClazz);

    CallResponseSpec result = chatClient.prompt()
        .system(systemMsg)
        .options(ChatOptions.builder().temperature(temperature).build())
        .user(userSpec -> userSpec
            .text(userMsg)
            .param("format", converter.getFormat())
            .params(jsonModel))
        .call();

    ResponseEntity<ChatResponse, R> chatResponseEntity = result.responseEntity(converter);
    return AssistantResponse.of(chatResponseEntity.getEntity());
  }

  private <R> AssistantResponse<R> fallback(
      String systemMsg, String userMsg, Map<String, Object> model, Class<R> responseClazz, Throwable t) {

    throw AssistantException.from(AssistantErrorCode.SERVICE_UNAVAILABLE);
  }
}
