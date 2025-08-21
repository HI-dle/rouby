package com.rouby.assistant.prompt.domain;

import com.rouby.assistant.prompt.domain.enums.PromptType;
import java.util.Optional;

public interface PromptRepository {
  Prompt save(Prompt prompt);

  Optional<Prompt> findByPromptTypeAndVersion(PromptType promptType, Integer version);
}
