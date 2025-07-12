package com.rouby.notification.email.application.messaging;

import jakarta.mail.MessagingException;

public interface EmailSender {

  void send(String to, String subject, String text) throws MessagingException;

}
