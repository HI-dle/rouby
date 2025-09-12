package com.rouby.batch.job.briefing.step;

import static com.rouby.notification.notificationtemplate.domain.entity.NotificationType.BRIEFING;

import com.rouby.assistant.briefing.application.dto.info.CreatedBriefingResult;
import com.rouby.assistant.briefing.application.facade.BriefingFacade;
import com.rouby.batch.job.briefing.dto.BriefingAggregate;
import com.rouby.batch.job.briefing.dto.BriefingNotificationEvents;
import com.rouby.batch.job.briefing.dto.UserBriefingInfo;
import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationType;
import com.rouby.notification.notificationtemplate.application.dto.query.NotificationMessageQuery;
import com.rouby.notification.notificationtemplate.application.service.NotificationTemplateReadService;
import com.rouby.notification.notificationtemplate.domain.entity.Message;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BriefingProcessor implements ItemProcessor<UserBriefingInfo, BriefingAggregate> {

  private static final String BriefingUrlPrefix = "/briefing/daily/";

  private final BriefingFacade briefingFacade;
  private final NotificationTemplateReadService notificationTemplateReadService;

  @Override
  public BriefingAggregate process(UserBriefingInfo user) {
    CreatedBriefingResult briefing = briefingFacade.createBriefingForBatch(user);

    Message message = notificationTemplateReadService.generateNotificationMessage(
        new NotificationMessageQuery(user.userInfo().nickname(), BRIEFING)
    );

    List<DeviceTokenInfo> deviceTokenInfos = Optional.ofNullable(user.devices())
        .orElseGet(List::of)
        .stream()
        .map(device -> DeviceTokenInfo.of(
            device.getTokenInfo().getDeviceToken(),
            device.getTokenInfo().getTokenProvider().name()
        ))
        .toList();

    BriefingNotificationEvents events = BriefingNotificationEvents.builder()
        .userId(user.userInfo().id())
        .deviceTokenInfos(deviceTokenInfos)
        .title(message.getTitle())
        .body(message.getBody())
        .url(BriefingUrlPrefix + user.today().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .notificationType(NotificationType.BRIEFING)
        .build();

    return new BriefingAggregate(briefing, events);
  }
}
