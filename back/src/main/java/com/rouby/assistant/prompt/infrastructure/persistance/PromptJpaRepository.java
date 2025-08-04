package com.rouby.assistant.prompt.infrastructure.persistance;

import com.rouby.assistant.prompt.domain.Prompt;
import com.rouby.assistant.prompt.domain.PromptRepository;
import com.rouby.assistant.prompt.domain.enums.PromptType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptJpaRepository extends JpaRepository<Prompt, Long>,
    PromptRepository, PromptJpaRepositoryCustom {
  Optional<Prompt> findByPromptTypeAndVersion(PromptType promptType, Integer version);
}
