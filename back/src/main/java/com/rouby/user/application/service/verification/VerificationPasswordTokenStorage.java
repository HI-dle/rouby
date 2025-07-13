package com.rouby.user.application.service.verification;

import java.util.Optional;

public interface VerificationPasswordTokenStorage {

  void storePasswordResetToken(String email, String token);

  Optional<String> getTokenByEmail(String email);

  void deleteByEmail(String email);
}
