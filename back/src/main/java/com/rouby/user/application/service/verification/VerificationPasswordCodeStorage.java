package com.rouby.user.application.service.verification;

import java.util.Optional;

public interface VerificationPasswordCodeStorage {

  void storePasswordResetToken(String email, String token);

  Optional<String> getTokenByEmail(String email);

  void delete(String token);
}
