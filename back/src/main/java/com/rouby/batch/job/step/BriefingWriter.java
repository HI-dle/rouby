package com.rouby.batch.job.step;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BriefingWriter implements ItemWriter<Briefing> {

  private final BriefingRepository briefingRepository;

  @Override
  public void write(Chunk<? extends Briefing> chunk) {
    log.info("Writing {} briefings", chunk.size());
    briefingRepository.saveAll(chunk.getItems());
  }
}
