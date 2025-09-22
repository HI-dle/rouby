package com.rouby.batch.briefing.step;

import com.rouby.batch.briefing.dto.UserBriefingInfo;
import com.rouby.user.device.application.dto.command.GetUserDeviceQuery;
import com.rouby.user.device.application.service.UserDeviceReadService;
import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.user.application.dto.info.UserInfo;
import com.rouby.user.user.application.service.UserReadService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class BriefingReader extends ListItemReader<UserBriefingInfo> {

  public BriefingReader(UserReadService userReadService,
      UserDeviceReadService userDeviceReadService,
      @Value("#{jobParameters['targetTime']}") LocalTime targetTime,
      @Value("#{jobParameters['today']}") LocalDate today) {
    super(fetchUsers(userReadService, userDeviceReadService, targetTime, today));
  }

  private static List<UserBriefingInfo> fetchUsers(UserReadService userReadService,
      UserDeviceReadService userDeviceReadService, LocalTime targetTime, LocalDate today) {

    List<UserInfo> userInfos = userReadService.findUsersByBriefingTime(targetTime);
    log.info("Found {} users", userInfos.size());

    if (userInfos.isEmpty()) {
      return List.of();
    }

    List<Long> userIds = userInfos.stream().map(UserInfo::id).toList();
    List<UserDevice> userDevices = userDeviceReadService.findByUserIds(
        new GetUserDeviceQuery(userIds));

    Map<Long, List<UserDevice>> deviceMap = userDevices.stream()
        .collect(Collectors.groupingBy(UserDevice::getUserId));

    return userInfos.stream()
        .map(user -> new UserBriefingInfo(
            user, deviceMap.getOrDefault(user.id(), List.of()), today, targetTime))
        .toList();
  }
}
