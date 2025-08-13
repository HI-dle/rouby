package com.rouby.user.device.application.scheduler;

import com.rouby.user.device.application.service.UserDeviceWriteService;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeviceScheduler {

  private static final Period STALE_THRESHOLD = Period.ofDays(60);
  private final UserDeviceWriteService userDeviceWriteService;

  @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
  public void deleteStaleDeviceTokens() {

    try {
      int deletedCnt = userDeviceWriteService.deleteStaleDeviceTokens(STALE_THRESHOLD);
      log.info("::stale device tokens:: {} 건 삭제되었습니다.", deletedCnt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
