package com.rouby.user.infrastructure.token;

import com.rouby.common.utils.AesCryptoUtils;
import com.rouby.common.utils.HmacSignUtils;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AesHmacVerificationTokenProvider {

  private static final String TOKEN_PREFIX = "EmailVerification ";
  private static final Base64.Encoder ENCODER = Base64.getEncoder();
  private static final Base64.Decoder DECODER = Base64.getDecoder();

  @Value("${verification-token.aes-secret}")
  private String aesKeyRaw;
  @Value("${verification-token.hmac-secret}")
  private String hmacKeyRaw;
  @Value("${verification-token.expiration}")
  private long expirationMillis;

  private AesCryptoUtils cryptoUtils;
  private HmacSignUtils signUtils;

  @PostConstruct
  private void init() {
    this.cryptoUtils = new AesCryptoUtils(aesKeyRaw);
    this.signUtils = new HmacSignUtils(hmacKeyRaw);
  }

  public String createVerificationToken(String email) {
    if (email == null) {
      throw new IllegalArgumentException("이메일은 null이 될 수 없습니다.");
    }
    VerificationPayload payload = new VerificationPayload(email,
        System.currentTimeMillis() + expirationMillis);
    try {
      byte[] encrypted = cryptoUtils.encrypt(payload.serialize());
      byte[] signature = signUtils.sign(encrypted);
      return TOKEN_PREFIX + encode(encrypted) + "." + encode(signature);
    } catch (Exception e) {
      throw new RuntimeException("토큰 생성 실패", e);
    }
  }

  public boolean validateVerificationToken(String token) {
    TokenParts parts = parse(token);
    if (parts == null) {
      return false;
    }
    try {
      if (!signUtils.verify(parts.encrypted(), parts.signature())) {
        return false;
      }
      VerificationPayload payload = deserializeSafely(parts.encrypted());
      return payload != null && payload.exp() > System.currentTimeMillis();
    } catch (Exception e) {
      return false;
    }
  }

  public String extractEmail(String token) {
    TokenParts parts = parse(token);
    try {
      if (parts == null || !signUtils.verify(parts.encrypted(), parts.signature())) {
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
      }

      VerificationPayload payload = deserializeSafely(parts.encrypted());
      if (payload == null) {
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
      }
      return payload.email();
    } catch (Exception e) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.", e);
    }
  }

  private VerificationPayload deserializeSafely(byte[] encrypted) {
    try {
      String plain = cryptoUtils.decrypt(encrypted);
      return VerificationPayload.deserialize(plain);
    } catch (Exception e) {
      log.debug("토큰 역직렬화 실패: {}", e.getMessage());
      return null;
    }
  }

  private TokenParts parse(String token) {
    if (token == null || !token.startsWith(TOKEN_PREFIX)) {
      return null;
    }
    String rawToken = token.substring(TOKEN_PREFIX.length());

    String[] parts = rawToken.split("\\.", 2);
    if (parts.length != 2) {
      return null;
    }

    try {
      byte[] encrypted = DECODER.decode(parts[0]);
      byte[] signature = DECODER.decode(parts[1]);
      return new TokenParts(encrypted, signature);
    } catch (IllegalArgumentException e) {
      log.debug("토큰 파싱 실패: {}", e.getMessage());
      return null;
    }
  }

  private String encode(byte[] data) {
    return ENCODER.encodeToString(data);
  }

  private record VerificationPayload(String email, long exp) {

    String serialize() {
      return email + ":" + exp;
    }

    static VerificationPayload deserialize(String input) {
      int lastColonIndex = input.lastIndexOf(':');
      if (lastColonIndex == -1) {
        throw new IllegalArgumentException("유효하지 않은 페이로드 형식입니다.");
      }
      String emailPart = input.substring(0, lastColonIndex);
      String expPart = input.substring(lastColonIndex + 1);

      try {
        long exp = Long.parseLong(expPart);
        return new VerificationPayload(emailPart, exp);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("유효하지 않은 페이로드 형식입니다.");
      }
    }
  }

  private record TokenParts(byte[] encrypted, byte[] signature) {

  }
}
