package com.rouby.user.application;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.rouby.user.application.dto.command.ResetPasswordCommand;


@Service
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;

	public void resetPassword(Long userId, ResetPasswordCommand command) {
		userService.resetPassword(userId, command);
	}
}
