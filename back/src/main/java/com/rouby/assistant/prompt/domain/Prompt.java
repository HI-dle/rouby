package com.rouby.assistant.prompt.domain;

import com.rouby.assistant.prompt.domain.enums.PromptType;
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
  public Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  public PromptType promptType;

  @Column(nullable = false, columnDefinition = "TEXT")
  public String promptTemplate;

  @Column(nullable = false)
  public Integer version;

  @Builder
  private Prompt(Long id, PromptType promptType, String promptTemplate, Integer version) {
    this.id = id;
    this.promptType = promptType;
    this.promptTemplate = promptTemplate;
    this.version = version;
  }

}
