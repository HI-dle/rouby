package com.rouby.assistant.briefing.domain.repository;

import com.rouby.assistant.briefing.domain.Briefing;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @Date : 2025. 07. 07.
 * @author : hanjihoon
 */
public interface BriefingRepository {
  <S extends Briefing> List<S> saveAll(Iterable<S> entities);

  Briefing save(Briefing briefing);

  Optional<Briefing> findByUserIdAndDate(Long userId, LocalDate date);
}
