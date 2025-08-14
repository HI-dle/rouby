package com.rouby.assistant.prompt.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.assistant.prompt.application.info.PromptInfo;
import com.rouby.assistant.prompt.domain.enums.PromptType;
import com.rouby.assistant.prompt.presentation.request.CreatePromptRequest;
import com.rouby.common.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class PromptControllerTest extends ControllerTestSupport {

  @Test
  @DisplayName("프롬프트 생성")
  void createUser() throws Exception {

    // given
    CreatePromptRequest request = new CreatePromptRequest(
        PromptType.BRIEFING,
        getPrompt(),
        1
    );
    given(promptFacade.createPrompt(request.toCommand())) .willReturn(new PromptInfo(
        1L,
        request.promptType(),
        request.promptTemplate(),
        request.version()
    ));


    // when and then
    mockMvc.perform(post("/api/v1/assistants/prompt")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
        )
        .andExpect(status().isCreated())
        .andDo(print())
        .andDo(document("prompt-create",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("promptType").description("프롬프트 타입(브리핑,피드백,추천)"),
                fieldWithPath("promptTemplate").description("프롬프트 템플릿"),
                fieldWithPath("version").description("프롬프트 버전")
            )
        ));
  }

  private String getPrompt() {
    return """
            [루비의 아침 브리핑 요청]
        
        
            오늘 날짜: %s
            사용자 이름: %s
            말투 스타일: %s
            프로필 키워드: %s
            건강 키워드: %s
        
            이번주 일정 요약:
            %s
        
        📝 요청사항:
            - 위 정보를 바탕으로, 사용자가 하루를 기분 좋게 시작할 수 있도록 따뜻하고 간결한 인사말과 오늘 해야 할 일의 요약 브리핑을 제공해주세요.
            - 위 일정의 recurrenceRule(RFC5545) 정보를 참고하여, 오늘 해야 할 일 위주로 작성하되, 앞으로 일주일 동안 중요한 일정들도 간략히 요약해 주세요.
                - 오늘 일정 및 이번주 일정 요약은 시간 순으로 정렬된 요약을 포함하고, 너무 기술적인 표현은 피해주세요.
                - 사용자의 프로필 키워드와 건강키워드를 참고해서 필요 시 루틴 유지 팁이나 격려 멘트도 추가해주세요.
            - 전체 결과는 하나의 자연스러운 텍스트로 구성해주세요. (JSON 아님)
            - 시간을 정확히 계산해주세요 이번주가 아닌 데이터들은 작성하지 않습니다(매주 시작은 월요일).
        
           출력 예시:
            ??님, 안녕하세요! 루비예요. 😊 오늘 하루도 활기차게 시작할 준비 되셨나요?
        
            오늘 2025년 8월 1일 금요일, ??님의 하루를 응원하는 루비의 브리핑 시작할게요!
        
                **오늘의 일정 요약**
        
        * **오전 7시: 아침 운동 루틴** - 월요일과 수요일에 이어, 오늘도 1시간 스트레칭과 유산소 운동으로 몸을 깨워주세요! 건강한 하루를 위한 최고의 시작이 될 거예요. 💪
        * **오전 10시: 업무 주간 회의** - 팀 전체 주간 회의가 있어요. 진행 상황을 공유하고, 이슈를 정리하는 중요한 시간이죠. 잊지 말고 Zoom 링크 확인하시구요!
        
                **이번 주, 일정 요약!**
                1. 8월 2일 토요일 7시 친구약속
            2. 8월 3일 일요일 18시 명상
        
            이번 주에는 운동 루틴을 꾸준히 실천하는 게 중요해요! 특히 월요일과 수요일에 잊지 말고 스트레칭과 유산소 운동을 해주세요. 건강한 몸은 활기찬 하루를 위한 기본이니까요! 🎵 좋아하는 음악을 들으면서 운동하면 더욱 즐겁게 할 수 있을 거예요.
        
            오늘 하루도 ??님만의 멋진 음악과 함께, 건강하고 행복하게 보내세요! 루비가 항상 응원할게요!
        """;
  }
}
