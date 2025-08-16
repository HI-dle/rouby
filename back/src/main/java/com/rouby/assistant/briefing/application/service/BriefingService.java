package com.rouby.assistant.briefing.application.service;

import com.rouby.assistant.prompt.application.client.BriefingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BriefingService {

  private final BriefingClient briefingClient;

  public String sendPromptToAi(String prompt) {
    return briefingClient.sendPromptToAi(prompt);
  }
}
