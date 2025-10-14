package com.rouby.assistant.prompt.domain.repository;

import com.rouby.assistant.prompt.domain.entity.Prompt;
import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import java.util.Optional;

public interface PromptRepository {
  
  Prompt save(Prompt prompt);

  Optional<Prompt> findByPromptTypeAndVersion(PromptType promptType, Integer version);

  Optional<Integer> findMaxVersionByPromptType(PromptType promptType);
}
