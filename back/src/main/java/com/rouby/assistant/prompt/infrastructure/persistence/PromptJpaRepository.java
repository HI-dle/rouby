package com.rouby.assistant.prompt.infrastructure.persistence;

import com.rouby.assistant.prompt.domain.entity.Prompt;
import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import com.rouby.assistant.prompt.domain.repository.PromptRepository;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PromptJpaRepository extends JpaRepository<Prompt, Long>,
    PromptRepository, PromptJpaRepositoryCustom {
  Optional<Prompt> findByPromptTypeAndVersion(PromptType promptType, Integer version);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
  SELECT MAX(p.version) FROM Prompt p
  WHERE p.promptType = :promptType
  """)
  Optional<Integer> findMaxVersionByPromptType(PromptType promptType);
}
