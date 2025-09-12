package com.rouby.assistant.briefing.application.service;

import com.rouby.assistant.briefing.application.dto.info.BriefingInfo;
import com.rouby.assistant.briefing.application.exception.BriefingErrorCode;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import com.rouby.common.exception.CustomException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BriefingReadService {

  private final BriefingRepository briefingRepository;

  public BriefingInfo getBriefingByDate(Long userId, LocalDate date) {
    return BriefingInfo.from(briefingRepository.findByUserIdAndDate(userId, date).orElseThrow(()
        -> CustomException.from(BriefingErrorCode.BRIEFING_NOT_FOUND)));
  }
}
