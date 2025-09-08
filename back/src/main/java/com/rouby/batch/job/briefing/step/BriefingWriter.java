package com.rouby.batch.job.briefing.step;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import com.rouby.batch.job.briefing.dto.BriefingAggregate;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    List<Briefing> briefings = new ArrayList<>();
    for (BriefingAggregate agg : chunk) {
      briefings.add(agg.briefing().toEntity());
    }

    List<Briefing> savedBriefings = briefingRepository.saveAll(briefings);

    Map<BriefingAggregate, Briefing> savedMap = new HashMap<>();
    int index = 0;
    for (BriefingAggregate agg : chunk) {
      savedMap.put(agg, savedBriefings.get(index++));
    }

    List<NotificationEvent> allEvents = new ArrayList<>();
    for (BriefingAggregate agg : chunk) {
      Briefing saved = savedMap.get(agg);

      List<NotificationEvent> events = agg.notificationEvents();
      events.forEach(event -> event.updateMessageUrl("/briefing/{" + saved.getId() + "}"));

      allEvents.addAll(events);

      log.info("Prepared briefing {} with {} notifications", saved.getId(), events.size());
    }

    notificationEventRepository.saveAll(allEvents);

    log.info("Saved {} briefings and {} notifications in batch", savedBriefings.size(),
        allEvents.size());
  }
}

