package com.rouby.notification.email.application.utils;

import com.rouby.notification.email.application.dto.EmailData;

public interface EmailTemplateCreator {

  String createHtml(EmailData data, String type);

  String createTitle(String type);
}
