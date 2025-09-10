package com.rouby.batch.job.briefing.step;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import com.rouby.batch.job.briefing.dto.BriefingAggregate;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BriefingWriter implements ItemWriter<BriefingAggregate> {

  private final BriefingRepository briefingRepository;
  private final NotificationEventRepository notificationEventRepository;

  @Override
  public void write(Chunk<? extends BriefingAggregate> chunk) {
    List<Briefing> briefings = chunk.getItems().stream()
        .map(agg -> agg.briefing().toEntity())
        .toList();
    List<Briefing> savedBriefings = briefingRepository.saveAll(briefings);

    List<NotificationEvent> allEvents = new ArrayList<>();
    for (int i = 0; i < chunk.size(); i++) {
      BriefingAggregate agg = chunk.getItems().get(i);
      Briefing saved = savedBriefings.get(i);

      List<NotificationEvent> events = agg.notificationEvents() != null
          ? agg.notificationEvents().toEntities() : List.of();

      if (!events.isEmpty()) {
        events.forEach(e -> e.updateMessageUrl("/briefing/daily/" + saved.getId()));
      }
      allEvents.addAll(events);

      log.info("Prepared briefing {} with {} notifications", saved.getId(), events.size());
    }

    if (!allEvents.isEmpty()) {
      notificationEventRepository.saveAll(allEvents);
    }

    log.info("Saved {} briefings and {} notifications in batch",
        savedBriefings.size(), allEvents.size());
  }
}

