package com.rouby.auth.presentation.dto.request;

import com.rouby.auth.application.dto.request.LoginCommand;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
public record LoginRequest(String email, String password) {

  public LoginCommand toApplication(){
  return new LoginCommand(email, password);
  }
}

