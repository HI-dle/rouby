package com.rouby.batch.briefing.step;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import com.rouby.batch.briefing.dto.BriefingAggregate;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import java.util.List;
import java.util.stream.Stream;
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

    List<NotificationEvent> allEvents = chunk.getItems().stream()
        .flatMap(agg -> agg.notificationEvents() != null
            ? agg.notificationEvents().toEntities().stream()
            : Stream.empty())
        .toList();

    if (!allEvents.isEmpty()) {
      notificationEventRepository.saveAll(allEvents);
    }

    log.info("Saved {} briefings and {} notifications in batch",
        savedBriefings.size(), allEvents.size());
  }
}

