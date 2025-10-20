package com.rouby.assistant.feedback.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.assistant.feedback.application.dto.GetDailyFeedbacksInfo;
import com.rouby.assistant.feedback.application.dto.GetDailyFeedbacksInfo.GetFeedbackInfo;
import com.rouby.assistant.feedback.fixture.CreateDailyFeedbackRequestFixture;
import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
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

  @WithMockCustomUser
  @DisplayName("일간 피드백 리스트 요청 API: 성공 200")
  @Test
  void getDailyFeedbacks() throws Exception {

    GetDailyFeedbacksInfo feedbacksInfo = new GetDailyFeedbacksInfo(
        IntStream.range(0, 3)
            .mapToObj(i -> GetFeedbackInfo.builder()
                .slot(i + 1)
                .userMood("SOSO")
                .userInput("사용자 요청 입력값 " + (i + 1))
                .feedbackContent("피드백 응답 데이터 " + (i + 1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build())
            .toList());

    when(feedbackReadService.getDailyFeedbacks(eq(1L), any(LocalDate.class)))
        .thenReturn(feedbacksInfo);

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/assistants/feedbacks/daily/{date}", LocalDate.now())
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
    );

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-daily-feedbacks-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("date").description("조회할 날짜 (YYYY-MM-DD)")
            ),
            responseFields(
                fieldWithPath("feedbacks[].slot").description("요청 횟수 (최대 3)"),
                fieldWithPath("feedbacks[].userMood").description("사용자 기분 (TERRIBLE, BAD, SOSO, GOOD, EXCELLENT)"),
                fieldWithPath("feedbacks[].userInput").description("사용자 입력값"),
                fieldWithPath("feedbacks[].feedbackContent").description("피드백 내용"),
                fieldWithPath("feedbacks[].createdAt").description("피드백 생성 요청 일시 (예: 2025-09-15T10:00:00)"),
                fieldWithPath("feedbacks[].updatedAt").description("피드백 생성 일시 (예: 2025-09-15T10:00:00)")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("일간 피드백 리스트 요청 API: 잘못된 일자 요청으로 인한 실패 400")
  @Test
  void getDailyFeedbacksWithFutureDate_return400() throws Exception {

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/assistants/feedbacks/daily/{date}", LocalDate.now().plusDays(1))
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .header(HttpHeaders.ACCEPT_LANGUAGE, "ko-KR")
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("get-daily-feedbacks-invalid-date-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getValidationErrorResponseFieldSnippet()
            ));
  }
}