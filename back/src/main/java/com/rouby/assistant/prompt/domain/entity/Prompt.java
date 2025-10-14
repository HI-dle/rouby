package com.rouby.assistant.prompt.domain.entity;

import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Prompt extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PromptType promptType;

  @Column(columnDefinition = "TEXT")
  private String systemMessage;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String userMessage;

  @Column(nullable = false)
  private Integer version;

  @Builder
  private Prompt(PromptType promptType, String systemMessage, String userMessage, Integer version) {
    this.promptType = promptType;
    this.systemMessage = systemMessage;
    this.userMessage = userMessage;
    this.version = version;
  }
}
