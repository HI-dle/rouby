package com.rouby.notification.email.application.service;

import static com.rouby.notification.email.application.exception.EmailErrorCode.EMAIL_LOG_SAVE_FAILED;
import static com.rouby.notification.email.application.exception.EmailErrorCode.EMAIL_SEND_FAILED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.notification.email.application.dto.SendEmailCommand;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.notification.email.application.messaging.EmailSender;
import com.rouby.notification.email.application.utils.EmailHtmlCreator;
import com.rouby.notification.email.domain.entity.EmailAddress;
import com.rouby.notification.email.domain.entity.EmailContent;
import com.rouby.notification.email.domain.entity.EmailLog;
import com.rouby.notification.email.domain.entity.EmailType;
import com.rouby.notification.email.domain.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final EmailSender emailSender;
  private final EmailLogRepository emailLogRepository;
  private final EmailHtmlCreator emailHtmlCreator;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void send(SendEmailCommand command) {

    try {
       String content = emailHtmlCreator.createHtml(command.emailData(), command.type());
       emailSender.send(command.to(), command.subject(), content);
       saveSentLog(objectMapper.writeValueAsString(command.emailData()), command.to(),
           command.type());
    } catch (MailException e) {
      log.error("이메일 전송에 실패했습니다: {}", command.to());
      saveFailedLog(objectMapper.writeValueAsString(command.emailData()), command.to(),
          command.type());
      throw EmailException.from(EMAIL_SEND_FAILED);
    } catch (Exception e) {
      log.error("이메일 로그 저장에 실패하였습니다: {}", e.getMessage());
      throw EmailException.from(EMAIL_LOG_SAVE_FAILED);
    }
  }

  private void saveSentLog(String text, String email, String type) {
    emailLogRepository.save(EmailLog.sent(
        EmailContent.of(text), EmailAddress.of(email), EmailType.of(type)));
  }

  private void saveFailedLog(String text, String email, String type) {
    emailLogRepository.save(EmailLog.failed(
        EmailContent.of(text), EmailAddress.of(email), EmailType.of(type)));
  }
}
