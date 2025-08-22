package com.rouby.user.user.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class SecurityPasswordEncoderTest {
  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Test
  void encodeTest() {
    String passwd = "1234";
    String firstEncoded = passwordEncoder.encode(passwd);
    String secondEncoded = passwordEncoder.encode(passwd);

    System.out.println("first try: " + firstEncoded);
    System.out.println("second try: " + secondEncoded);

    assertThat(firstEncoded).isNotEqualTo(secondEncoded);
  }

  @Test
  void matchTest() {
    String passwd = "1234";

    assertThat(passwordEncoder.matches(passwd, passwordEncoder.encode(passwd))).isTrue();
  }
}