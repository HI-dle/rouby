package com.rouby.assistant.prompt.infrastructure.persistence;

import com.rouby.assistant.prompt.domain.entity.Prompt;
import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import com.rouby.assistant.prompt.domain.repository.PromptRepository;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromptJpaRepository extends JpaRepository<Prompt, Long>,
    PromptRepository, PromptJpaRepositoryCustom {
  Optional<Prompt> findByPromptTypeAndVersion(PromptType promptType, Integer version);

  @Query("""
  SELECT p.version FROM Prompt p
  WHERE p.promptType = :promptType
  ORDER BY p.version DESC
  LIMIT 1
  """)
  Optional<Integer> checkVersionByPromptType(PromptType promptType);
}
