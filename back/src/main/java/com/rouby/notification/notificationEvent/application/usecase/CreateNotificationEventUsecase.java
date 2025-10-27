package com.rouby.notification.notificationEvent.application.usecase;

import static com.rouby.notification.notificationtemplate.domain.entity.NotificationType.FEEDBACK;

import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventCommand;
import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventWithTemplateCommand;
import com.rouby.notification.notificationEvent.application.service.NotificationEventWriteService;
import com.rouby.notification.notificationEvent.domain.sender.NotificationSender;
import com.rouby.notification.notificationtemplate.application.dto.MessageInfo;
import com.rouby.notification.notificationtemplate.application.dto.query.NotificationMessageQuery;
import com.rouby.notification.notificationtemplate.application.service.NotificationTemplateReadService;
import java.time.Clock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationEventUsecase {

  private final NotificationTemplateReadService notificationTemplateReadService;
  private final NotificationEventWriteService notificationEventWriteService;

  private final NotificationSender notificationSender;
  private final Clock clock;

  public void create(List<CreateNotificationEventCommand> commands, Boolean notificationEnabled) {

    if (!notificationEnabled || commands.isEmpty()) {
      // todo 대안책으로 sse 전송 수행 필요
    }

    MessageInfo messageInfo = notificationTemplateReadService.generateNotificationMessage(
        new NotificationMessageQuery(commands.get(0).nickname(), FEEDBACK)
    );
    notificationEventWriteService.createEvents(commands.stream()
            .map(command ->
                CreateNotificationEventWithTemplateCommand.from(command, messageInfo))
            .toList(), notificationEnabled);
  }

  public void sendNotification(CreateNotificationEventWithTemplateCommand command) {

    this.notificationSender.send(command.toInfo(), 10, clock.millis(), false);
  }
}
