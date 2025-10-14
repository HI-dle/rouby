package com.rouby.assistant.feedback.fixture;

import com.rouby.assistant.feedback.presentation.dto.CreateFeedbackRequest;

public class CreateDailyFeedbackRequestFixture {

  public static CreateFeedbackRequest getSuccessRequest() {

    return CreateFeedbackRequest.builder()
        .userInput("오늘은 새로운 기능을 개발해서 뿌듯해.")
        .userMood("GOOD")
        .build();
  }

  public static CreateFeedbackRequest getInvalidRequest() {

    return CreateFeedbackRequest.builder()
        .userInput("오늘은 새로운 기능을 개발해서 뿌듯해.")
        .userMood("GREAT")
        .build();
  }
}
