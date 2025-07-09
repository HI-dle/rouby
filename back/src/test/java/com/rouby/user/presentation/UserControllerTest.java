package com.rouby.user.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.user.presentation.dto.request.ResetPasswordRequest;

class UserControllerTest extends ControllerTestSupport {

	@WithMockCustomUser
	@DisplayName("비밀번호 변경 API")
	@Test
	void resetPassword() throws Exception {

		//given
		ResetPasswordRequest request = ResetPasswordRequest.builder()
				.currentPassword("currentPassword")
				.newPassword("newPassword")
				.build();

		Long userId = 1L;

		doNothing().when(userFacade).resetPassword(eq(userId), any());

		//when
		ResultActions resultActions = mockMvc.perform(patch("/api/v1/users/reset-password")
				.header("Authorization", "Bearer {ACCESS_TOKEN}")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("resetPassword-user-200",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("currentPassword").description("현재 비밀번호"),
								fieldWithPath("newPassword").description("새 비밀번호"))
				));
	}
}