package com.rouby.test.data;

import com.rouby.notification.email.domain.entity.EmailAddress;
import com.rouby.notification.email.domain.entity.EmailContent;
import com.rouby.notification.email.domain.entity.EmailLog;
import com.rouby.notification.email.domain.entity.EmailType;
import com.rouby.notification.email.domain.entity.SendStatus;
import com.rouby.notification.email.infrastructure.persistence.jpa.EmailLogJpaRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Disabled
public class EmailLogInitRunner {

  @Autowired EmailLogJpaRepository emailLogRepository;

  @Test
  public void run() {
    List<EmailLog> logs = new ArrayList<>();
    for (int i = 0; i < 100_000; i++) {
      EmailContent content = EmailContent.of("content");
      EmailAddress address = EmailAddress.of("user" + (i % 1000) + "@example.com");

      EmailType type = EmailType.values()[i % EmailType.values().length];
      SendStatus status = (i % 10 == 0) ? SendStatus.FAILED : SendStatus.SENT;

      EmailLog log = EmailLog.builder()
          .content(content)
          .address(address)
          .status(status)
          .type(type)
          .build();

      logs.add(log);

      if (logs.size() == 1000) {
        emailLogRepository.saveAll(logs);
        logs.clear();
      }
    }

    if (!logs.isEmpty()) {
      emailLogRepository.saveAll(logs);
    }

    System.out.println("✅ 10만 건 삽입 완료");
  }
}