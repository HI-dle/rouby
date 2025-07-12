package com.rouby.notification.email.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.notification.email.application.dto.EmailData;
import com.rouby.notification.email.application.utils.EmailHtmlCreator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Component
public class EmailHtmlCreatorImpl implements EmailHtmlCreator {

  private final TemplateEngine templateEngine;
  private final ObjectMapper objectMapper;

  public String createHtml(EmailData data, String type) {

    Context context = new Context();
    Map<String, String> dataMap = objectMapper.convertValue(data, new TypeReference<>() {});
    for (Entry<String, String> item : dataMap.entrySet()) {
      context.setVariable(item.getKey(), item.getValue());
    }
    return templateEngine.process(TemplateType.from(type).getTemplateName(), context);
  }

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  enum TemplateType {

    VERIFICATION("email-verification"),
    RESET_PASSWORD("reset-password"),
    ;

    private final String templateName;

    static TemplateType from(String type) {

      try {
        return valueOf(type.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("유효하지 않은 템플릿 타입 입니다: " + type);
      }
    }
  }
}

