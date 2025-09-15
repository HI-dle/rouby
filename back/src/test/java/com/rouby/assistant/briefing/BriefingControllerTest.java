package com.rouby.assistant.briefing;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.assistant.briefing.application.dto.info.BriefingInfo;
import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class BriefingControllerTest extends ControllerTestSupport {

  @WithMockCustomUser
  @DisplayName("특정 날짜 브리핑 조회 성공")
  @Test
  void getBriefingByDate() throws Exception {

    // given
    Long userId = 1L;
    LocalDate date = LocalDate.of(2025, 8, 1);

    BriefingInfo briefingInfo = BriefingInfo.builder()
        .content("오늘 하루 브리핑")
        .createdAt(date.atStartOfDay())
        .build();

    when(briefingFacade.getBriefingByDate(userId, date)).thenReturn(briefingInfo);

    // when & then
    mockMvc.perform(get("/api/v1/assistants/briefings/{date}", date)
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-briefing-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("date").description("조회할 날짜 (YYYY-MM-DD)")
            ),
            responseFields(
                fieldWithPath("content").description("브리핑 내용"),
                fieldWithPath("createdAt").description("브리핑 생성 일시")
            )
        ));
  }
}
