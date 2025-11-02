package com.rouby.user.user.application.scheduler;

import com.rouby.user.user.application.service.UserWriteService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserScheduler {
  private final UserWriteService userWriteService;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  public void deleteExpiredRefreshTokens() {
    try {
      LocalDateTime cutoffTime = LocalDateTime.now();
      long count = userWriteService.deleteExpiredRefreshTokens(cutoffTime);
      log.info("[" + cutoffTime + "] 기준 만료된 RefreshToken 삭제 개수: " + count);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
