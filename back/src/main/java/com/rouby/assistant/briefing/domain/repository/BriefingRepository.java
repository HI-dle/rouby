package com.rouby.assistant.briefing.domain.repository;

import com.rouby.assistant.briefing.domain.Briefing;
import java.util.List;

/**
 * @Date : 2025. 07. 07.
 * @author : hanjihoon
 */
public interface BriefingRepository {
  <S extends Briefing> List<S> saveAll(Iterable<S> entities);

  Briefing save(Briefing briefing);
}
