package com.rouby.batch.job.step;

import com.rouby.assistant.briefing.domain.Briefing;
import com.rouby.assistant.briefing.domain.repository.BriefingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BriefingWriter implements ItemWriter<Briefing> {

  private final BriefingRepository briefingRepository;

  @Override
  public void write(Chunk<? extends Briefing> chunk) {
    briefingRepository.saveAll(chunk.getItems());
  }
}
