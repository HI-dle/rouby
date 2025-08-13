package com.rouby.batch.job.step;

import com.rouby.user.application.dto.info.UserInfo;
import com.rouby.user.application.service.UserReadService;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class BriefingReader extends ListItemReader<UserInfo> {

  public BriefingReader(UserReadService userReadService) {
    super(fetchUsers(userReadService));
  }

  private static List<UserInfo> fetchUsers(UserReadService userReadService) {
    LocalTime targetTime = LocalTime.of(8, 0); // 오전 8시
    log.info("Fetching users");
    List<UserInfo> userInfos = userReadService.findUserByBriefingTimeNow(targetTime);
    log.info("Found {} users", userInfos.size());
    return userInfos;
  }
}
