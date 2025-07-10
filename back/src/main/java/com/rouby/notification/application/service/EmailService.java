package com.rouby.notification.application.service;

import com.rouby.notification.application.service.event.SendEmailFailedEvent;
import com.rouby.notification.application.service.event.SendEmailSuccessEvent;
import com.rouby.user.application.dto.SendEmailCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EmailService {

  private final JavaMailSender mailSender;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void send(SendEmailCommand command) {
    String to = command.to();
    String subject = command.subject();
    String text = command.text();

    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);
      mailSender.send(message);
      eventPublisher.publishEvent(SendEmailSuccessEvent.of(to, text));
    } catch (MailException e) {
      eventPublisher.publishEvent(SendEmailFailedEvent.of(to, text));
    }
  }
}
