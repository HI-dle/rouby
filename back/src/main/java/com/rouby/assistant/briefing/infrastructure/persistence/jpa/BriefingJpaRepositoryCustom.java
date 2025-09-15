package com.rouby.assistant.briefing.infrastructure.persistence.jpa;

import com.rouby.assistant.briefing.domain.Briefing;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
public interface BriefingJpaRepositoryCustom {

  Optional<Briefing> findByUserIdAndDate(Long userId, LocalDate date);
}
