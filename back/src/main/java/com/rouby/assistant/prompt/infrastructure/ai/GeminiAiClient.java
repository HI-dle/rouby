package com.rouby.assistant.prompt.infrastructure.ai;

import com.rouby.assistant.prompt.application.client.BriefingClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeminiAiClient implements BriefingClient {
  private final ChatClient chatClient;

  @Override
  public String sendPromptToAi(String prompt) {
    try {
      ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();

      if (chatResponse == null) {
        throw new IllegalStateException("ChatResponse is null");
      }

      Generation result = chatResponse.getResult();
      if (result == null) {
        throw new IllegalStateException("No generation result from AI");
      }

      AssistantMessage output = result.getOutput();
      if (output == null || output.getText() == null) {
        throw new IllegalStateException("AI output message is empty");
      }

      return output.getText();

    } catch (Exception e) {
      log.error("AI 응답 처리 중 오류 발생: {}", e.getMessage(), e);
      throw new RuntimeException("AI 응답 처리 중 오류 발생", e);
    }
  }
}
