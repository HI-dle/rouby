package com.rouby.user.application.service.verification;

import java.util.Optional;

public interface VerificationEmailCodeStorage {

  void save(String email, VerificationEmailCode code);

  void verified(String email, VerificationEmailCode code);

  Optional<VerificationEmailCode> findByEmail(String email);

  void delete(String email);

}
