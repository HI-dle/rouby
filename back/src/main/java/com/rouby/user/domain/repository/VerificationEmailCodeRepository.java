package com.rouby.user.domain.repository;

import com.rouby.user.application.entity.VerificationEmailCode;
import java.util.Optional;

public interface VerificationEmailCodeRepository {

  void save(String email, VerificationEmailCode code);

  Optional<VerificationEmailCode> findByEmail(String email);

  void delete(String email);

}
