package com.rouby.notification.email.infrastructure.messaging;

import com.rouby.notification.email.application.messaging.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class JavaEmailSenderImpl implements EmailSender {

  private final JavaMailSender mailSender;

  @Override
  public void send(String to, String subject, String html) throws MessagingException {

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(html, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      log.error("메일 전송 실패: {}", e.getMessage());
      throw e;
    }
  }
}
