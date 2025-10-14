package com.rouby.assistant.feedback.presentation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.assistant.feedback.fixture.CreateDailyFeedbackRequestFixture;
import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class FeedbackControllerTest extends ControllerTestSupport {

  @WithMockCustomUser
  @DisplayName("피드백 요청 API: 성공 202")
  @Test
  void createDailyFeedback() throws Exception {

    var request = CreateDailyFeedbackRequestFixture.getSuccessRequest();
    var content = objectMapper.writeValueAsString(request);

    doNothing().when(createFeedbackUsecase).requestFeedback(eq(request.toCommand(1L)));

    // when
    ResultActions resultActions = mockMvc.perform(
        post("/api/v1/assistants/feedbacks")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(content)
            .characterEncoding("UTF-8")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isAccepted())
        .andDo(print())
        .andDo(document("create-feedback-202",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("userInput").description("사용자의 피드백 요청 관련 입력"),
                fieldWithPath("userMood").description("사용자의 기분 상태")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("피드백 요청 API: 유효하지 않은 요청으로 실패 400")
  @Test
  void failCreateDailyFeedbackWithInvalidRequest() throws Exception {

    var request = CreateDailyFeedbackRequestFixture.getInvalidRequest();
    var content = objectMapper.writeValueAsString(request);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/assistants/feedbacks")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("create-feedback-invalid-user-mood-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getValidationErrorResponseFieldSnippet()
        ));
  }
}