package com.rouby.assistant.briefing.infrastructure.persistence.jpa;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
public interface BriefingJpaRepository extends JpaRepository<Briefing, Long>,
    BriefingJpaRepositoryCustom,BriefingRepository {}
