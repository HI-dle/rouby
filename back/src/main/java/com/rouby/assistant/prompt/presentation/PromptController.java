package com.rouby.assistant.prompt.presentation;

import com.rouby.assistant.prompt.application.PromptFacade;
import com.rouby.assistant.prompt.presentation.request.CreatePromptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/assistants/prompt")
@RestController
@RequiredArgsConstructor
public class PromptController {

  private final PromptFacade promptFacade;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Void> createPrompt(@RequestBody CreatePromptRequest request) {
    promptFacade.createPrompt(request.toCommand());
    return ResponseEntity.ok().build();
  }
}
