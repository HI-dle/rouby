package com.rouby.notification.email.application.dto;

public interface SendEmailCommand {
  String to();
  String subject();
  String type();
  EmailData emailData();
}
