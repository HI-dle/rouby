package com.rouby.notification.email.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.notification.email.application.dto.EmailData;
import com.rouby.notification.email.application.utils.EmailTemplateCreator;
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
public class EmailTemplateCreatorImpl implements EmailTemplateCreator {

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

  public String createTitle(String type) {
    return TemplateType.from(type).subject;
  }

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  enum TemplateType {

    VERIFICATION("[Rouby] 이메일 인증 코드입니다.", "email-verification"),
    RESET_PASSWORD("[Rouby] 비밀번호 변경 링크 메일입니다.", "reset-password"),
    ;

    private final String subject;
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

