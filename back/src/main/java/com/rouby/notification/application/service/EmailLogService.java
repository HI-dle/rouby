package com.rouby.notification.application.service;

import com.rouby.notification.domain.entity.emaillog.EmailAddress;
import com.rouby.notification.domain.entity.emaillog.EmailContent;
import com.rouby.notification.domain.entity.emaillog.EmailLog;
import com.rouby.notification.domain.repository.emaillog.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailLogService {

  private final EmailLogRepository emailLogRepository;

  @Transactional
  public void createSentLog(CreateSentEmailLog command) {
    emailLogRepository.save(EmailLog.sent(
        EmailContent.of(command.content()), EmailAddress.of(command.address())));
  }

  @Transactional
  public void createFailedLog(CreateFailedEmailLog command) {
    emailLogRepository.save(EmailLog.failed(
        EmailContent.of(command.content()), EmailAddress.of(command.address())));
  }
}
