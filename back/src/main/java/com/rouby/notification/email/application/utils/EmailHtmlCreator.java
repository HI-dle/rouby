package com.rouby.notification.email.application.utils;

import com.rouby.notification.email.application.dto.EmailData;

public interface EmailHtmlCreator {

  String createHtml(EmailData data, String type);
}
