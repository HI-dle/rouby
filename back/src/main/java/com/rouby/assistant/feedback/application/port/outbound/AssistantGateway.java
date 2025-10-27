package com.rouby.assistant.feedback.application.port.outbound;

import com.rouby.assistant.feedback.application.dto.InfoForFeedback;
import java.util.concurrent.CompletableFuture;

public interface AssistantGateway {

  <R> CompletableFuture<R> requestDailyFeedbackAsync(
      int version, double temperature, InfoForFeedback command, Class<R> responseClazz);
}
