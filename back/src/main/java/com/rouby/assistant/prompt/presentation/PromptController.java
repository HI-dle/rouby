package com.rouby.assistant.prompt.presentation;

import com.rouby.assistant.prompt.application.service.PromptWriteService;
import com.rouby.assistant.prompt.presentation.request.CreatePromptRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

  private final PromptWriteService promptWriteService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Void> createPrompt(@RequestBody @Valid CreatePromptRequest request) {

    promptWriteService.createPrompt(request.toCommand());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
