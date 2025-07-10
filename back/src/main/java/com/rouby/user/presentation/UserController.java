package com.rouby.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.request.ResetPasswordRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserFacade userFacade;

	@PatchMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(
			@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid ResetPasswordRequest request) {

		userFacade.resetPassword(userDetails.getId(), request.toCommand());

		return ResponseEntity.ok().build();
	}
}
