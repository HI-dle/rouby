package com.rouby.assistant.prompt.infrastructure.ai;

import com.rouby.assistant.prompt.application.client.AssistantClient;
import com.rouby.assistant.prompt.domain.info.AssistantResponse;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantErrorCode;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantInfraException;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantInfraRetryableException;
import com.rouby.common.utils.JsonHelper;
import com.rouby.common.utils.RetryAfterParser;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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

    final Map<String, Object> jsonModel;
    BeanOutputConverter<R> converter;

    try {
      Map<String, Object> safeModel = (model != null) ? model : java.util.Collections.emptyMap();
      jsonModel = safeModel.entrySet().stream()
          .collect(Collectors.toMap(
              Entry::getKey, entry-> jsonHelper.toJson(entry.getValue())));
       converter = responseConverterManager.getConverter(responseClazz);

    } catch (Exception e) {
      throw AssistantInfraException.from(AssistantErrorCode.ASSISTANT_INVALID_REQUEST);
    }

    try {
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

    } catch (HttpClientErrorException e) {

      if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
        long retryAfterSeconds = RetryAfterParser.parseRetryAfterSeconds(e.getResponseHeaders());
        if (retryAfterSeconds > -1 && retryAfterSeconds <= 5) {
          throw AssistantInfraRetryableException.of(
              HttpStatus.TOO_MANY_REQUESTS, e.getMessage(),
              RetryAfterParser.parseRetryAfterSeconds(e.getResponseHeaders()));
        }
      }
      throw AssistantInfraException.of(HttpStatus.valueOf(e.getStatusCode().value()), e.getMessage());
    }
  }

  private <R> AssistantResponse<R> fallback(
      double temperature, String systemMsg, String userMsg,
      Map<String, Object> model, Class<R> responseClazz, Throwable t) {

    if (t instanceof CallNotPermittedException) {
      log.error("Assistant 요청 실패로 인한 서킷 브레이커 활성화", t);
      throw AssistantInfraException.from(AssistantErrorCode.SERVICE_UNAVAILABLE);
    }
    log.error("Assistant 생성 요청 실패", t);
    if (t instanceof RuntimeException re) throw re;
    throw new RuntimeException(t);
  }
}
