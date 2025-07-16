package com.rouby.user.infrastructure.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AesHmacVerificationTokenProviderTest {

  private AesHmacVerificationTokenProvider tokenProvider;

  private static final String VALID_AES_KEY = "test-aes-key-123456";
  private static final String VALID_HMAC_KEY = "test-hmac-key-for-signing";
  private static final long DEFAULT_EXPIRATION = 300000L;

  @BeforeEach
  void setUp() {
    tokenProvider = new AesHmacVerificationTokenProvider();

    ReflectionTestUtils.setField(tokenProvider, "aesKeyRaw", VALID_AES_KEY);
    ReflectionTestUtils.setField(tokenProvider, "hmacKeyRaw", VALID_HMAC_KEY);
    ReflectionTestUtils.setField(tokenProvider, "expirationMillis", DEFAULT_EXPIRATION);

    ReflectionTestUtils.invokeMethod(tokenProvider, "initKey");
  }

  @Nested
  @DisplayName("초기화 테스트")
  class InitializationTest {

    @Test
    @DisplayName("정상적인 AES 키로 초기화 성공")
    void initKey_WithValidKey_Success() {
      // given
      AesHmacVerificationTokenProvider provider = new AesHmacVerificationTokenProvider();
      ReflectionTestUtils.setField(provider, "aesKeyRaw", "valid-16byte-key");
      ReflectionTestUtils.setField(provider, "hmacKeyRaw", "valid-16byte-key");

      // when & then
      assertDoesNotThrow(() -> ReflectionTestUtils.invokeMethod(provider, "initKey"));
    }

    @Test
    @DisplayName("짧은 AES 키로 초기화 실패")
    void initKey_WithShortKey_ThrowsException() {
      // given
      AesHmacVerificationTokenProvider provider = new AesHmacVerificationTokenProvider();
      ReflectionTestUtils.setField(provider, "aesKeyRaw", "short");

      // when & then
      IllegalStateException exception = assertThrows(
          IllegalStateException.class,
          () -> ReflectionTestUtils.invokeMethod(provider, "initKey")
      );
      assertThat(exception.getMessage()).contains("AES 키는 최소 16바이트 이상이어야 합니다");
    }

    @Test
    @DisplayName("긴 AES 키는 16바이트로 자동 잘림")
    void initKey_WithLongKey_TruncatedTo16Bytes() {
      // given
      AesHmacVerificationTokenProvider provider = new AesHmacVerificationTokenProvider();
      String longKey = "this-is-a-very-long-key-more-than-16-bytes";
      ReflectionTestUtils.setField(provider, "aesKeyRaw", longKey);
      ReflectionTestUtils.setField(provider, "hmacKeyRaw", longKey);

      // when
      ReflectionTestUtils.invokeMethod(provider, "initKey");
      // then
      byte[] aesKey = (byte[]) ReflectionTestUtils.getField(provider, "aesKey");
      assertThat(aesKey).hasSize(16);
    }
  }

  @Nested
  @DisplayName("토큰 생성 테스트")
  class TokenCreationTest {

    @Test
    @DisplayName("유효한 이메일로 토큰 생성 성공")
    void createVerificationToken_WithValidEmail_Success() {
      // given
      String email = "test@example.com";

      // when
      String token = tokenProvider.createVerificationToken(email);

      // then
      assertThat(token).isNotNull();
      assertThat(token).contains(".");

      // 토큰 형식 검증
      String[] parts = token.substring("EmailVerification ".length()).split("\\.");
      assertThat(parts).hasSize(2);
      assertDoesNotThrow(() -> Base64.getDecoder().decode(parts[0]));
      assertDoesNotThrow(() -> Base64.getDecoder().decode(parts[1]));
    }

    @Test
    @DisplayName("특수문자가 포함된 이메일로 토큰 생성")
    void createVerificationToken_WithSpecialCharacters_Success() {
      // given
      String email = "test+special@example-domain.co.kr";

      // when
      String token = tokenProvider.createVerificationToken(email);

      // then
      assertThat(token).isNotNull();
      assertThat(tokenProvider.extractEmail(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("빈 이메일로 토큰 생성")
    void createVerificationToken_WithEmptyEmail_Success() {
      // given
      String email = "";

      // when
      String token = tokenProvider.createVerificationToken(email);

      // then
      assertThat(token).isNotNull();
      assertThat(tokenProvider.extractEmail(token)).isEqualTo(email);
    }
  }

  @Nested
  @DisplayName("토큰 검증 테스트")
  class TokenValidationTest {

    @Test
    @DisplayName("유효한 토큰 검증 성공")
    void validateVerificationToken_WithValidToken_ReturnsTrue() {
      // given
      String email = "test@example.com";
      String token = tokenProvider.createVerificationToken(email);

      // when
      boolean isValid = tokenProvider.validateVerificationToken(token);

      // then
      assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰 검증 실패")
    void validateVerificationToken_WithExpiredToken_ReturnsFalse() {
      // given
      AesHmacVerificationTokenProvider shortExpirationProvider = new AesHmacVerificationTokenProvider();
      ReflectionTestUtils.setField(shortExpirationProvider, "aesKeyRaw", VALID_AES_KEY);
      ReflectionTestUtils.setField(shortExpirationProvider, "hmacKeyRaw", VALID_HMAC_KEY);
      ReflectionTestUtils.setField(shortExpirationProvider, "expirationMillis", 1L);
      ReflectionTestUtils.invokeMethod(shortExpirationProvider, "initKey");

      String email = "test@example.com";
      String token = shortExpirationProvider.createVerificationToken(email);

      // when - 만료 대기
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      boolean isValid = shortExpirationProvider.validateVerificationToken(token);

      // then
      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("잘못된 형식의 토큰 검증 실패")
    void validateVerificationToken_WithInvalidFormat_ReturnsFalse() {
      // given
      String invalidToken = "invalid.token.format";

      // when
      boolean isValid = tokenProvider.validateVerificationToken(invalidToken);

      // then
      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("점이 없는 토큰 검증 실패")
    void validateVerificationToken_WithoutDot_ReturnsFalse() {
      // given
      String tokenWithoutDot = "invalidtokenformat";

      // when
      boolean isValid = tokenProvider.validateVerificationToken(tokenWithoutDot);

      // then
      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Base64 디코딩 실패하는 토큰 검증 실패")
    void validateVerificationToken_WithInvalidBase64_ReturnsFalse() {
      // given
      String invalidBase64Token = "invalid@base64.invalid@base64";

      // when
      boolean isValid = tokenProvider.validateVerificationToken(invalidBase64Token);

      // then
      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("서명이 변조된 토큰 검증 실패")
    void validateVerificationToken_WithTamperedSignature_ReturnsFalse() {
      // given
      String email = "test@example.com";
      String originalToken = tokenProvider.createVerificationToken(email);
      String[] parts = originalToken.split("\\.");

      // 서명 부분을 변조
      String tamperedSignature = Base64.getEncoder().encodeToString("tampered".getBytes());
      String tamperedToken = parts[0] + "." + tamperedSignature;

      // when
      boolean isValid = tokenProvider.validateVerificationToken(tamperedToken);

      // then
      assertThat(isValid).isFalse();
    }
  }

  @Nested
  @DisplayName("이메일 추출 테스트")
  class EmailExtractionTest {

    @Test
    @DisplayName("유효한 토큰에서 이메일 추출 성공")
    void extractEmail_WithValidToken_ReturnsEmail() {
      // given
      String expectedEmail = "test@example.com";
      String token = tokenProvider.createVerificationToken(expectedEmail);

      // when
      String extractedEmail = tokenProvider.extractEmail(token);

      // then
      assertThat(extractedEmail).isEqualTo(expectedEmail);
    }

    @Test
    @DisplayName("유효하지 않은 토큰에서 이메일 추출 시 예외 발생")
    void extractEmail_WithInvalidToken_ThrowsException() {
      // given
      String invalidToken = "invalid.token";

      // when & then
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> tokenProvider.extractEmail(invalidToken)
      );
      assertThat(exception.getMessage()).contains("유효하지 않은 토큰입니다");
    }

    @Test
    @DisplayName("서명이 변조된 토큰에서 이메일 추출 시 예외 발생")
    void extractEmail_WithTamperedToken_ThrowsException() {
      // given
      String email = "test@example.com";
      String originalToken = tokenProvider.createVerificationToken(email);
      String[] parts = originalToken.split("\\.");

      // 암호화된 부분을 변조
      String tamperedEncrypted = Base64.getEncoder().encodeToString("tampered".getBytes());
      String tamperedToken = tamperedEncrypted + "." + parts[1];

      // when & then
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> tokenProvider.extractEmail(tamperedToken)
      );
      assertThat(exception.getMessage()).contains("유효하지 않은 토큰입니다");
    }
  }

  @Nested
  @DisplayName("암호화/복호화 테스트")
  class EncryptionTest {

    @Test
    @DisplayName("동일한 이메일로 생성한 토큰들은 서로 다름 (시간 차이)")
    void createVerificationToken_SameEmail_DifferentTokens() throws InterruptedException {
      // given
      String email = "test@example.com";

      // when
      String token1 = tokenProvider.createVerificationToken(email);
      Thread.sleep(1);
      String token2 = tokenProvider.createVerificationToken(email);

      // then
      assertThat(token1).isNotEqualTo(token2);
      assertThat(tokenProvider.extractEmail(token1)).isEqualTo(email);
      assertThat(tokenProvider.extractEmail(token2)).isEqualTo(email);
    }
  }

  @Nested
  @DisplayName("보안 테스트")
  class SecurityTest {

    @Test
    @DisplayName("다른 HMAC 키로 생성된 토큰은 검증 실패")
    void validateToken_WithDifferentHmacKey_ReturnsFalse() {
      // given - 다른 HMAC 키를 가진 provider
      AesHmacVerificationTokenProvider otherProvider = new AesHmacVerificationTokenProvider();
      ReflectionTestUtils.setField(otherProvider, "aesKeyRaw", VALID_AES_KEY);
      ReflectionTestUtils.setField(otherProvider, "hmacKeyRaw", "different-hmac-key");
      ReflectionTestUtils.setField(otherProvider, "expirationMillis", DEFAULT_EXPIRATION);
      ReflectionTestUtils.invokeMethod(otherProvider, "initKey");

      String email = "test@example.com";
      String token = otherProvider.createVerificationToken(email);

      // when
      boolean isValid = tokenProvider.validateVerificationToken(token);

      // then
      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("다른 AES 키로 생성된 토큰은 검증 실패")
    void validateToken_WithDifferentAesKey_ReturnsFalse() {
      // given
      AesHmacVerificationTokenProvider otherProvider = new AesHmacVerificationTokenProvider();
      ReflectionTestUtils.setField(otherProvider, "aesKeyRaw", "different-aes-key-123");
      ReflectionTestUtils.setField(otherProvider, "hmacKeyRaw", VALID_HMAC_KEY);
      ReflectionTestUtils.setField(otherProvider, "expirationMillis", DEFAULT_EXPIRATION);
      ReflectionTestUtils.invokeMethod(otherProvider, "initKey");

      String email = "test@example.com";
      String token = otherProvider.createVerificationToken(email);

      // when
      boolean isValid = tokenProvider.validateVerificationToken(token);

      // then
      assertThat(isValid).isFalse();
    }
  }

  @Nested
  @DisplayName("통합 테스트")
  class IntegrationTest {

    @Test
    @DisplayName("토큰 생성 -> 검증 -> 이메일 추출 전체 플로우")
    void fullTokenWorkflow_Success() {
      // given
      String email = "integration.test@example.com";

      // when
      String token = tokenProvider.createVerificationToken(email);
      boolean isValid = tokenProvider.validateVerificationToken(token);
      String extractedEmail = tokenProvider.extractEmail(token);

      // then
      assertThat(isValid).isTrue();
      assertThat(extractedEmail).isEqualTo(email);
    }
  }
}