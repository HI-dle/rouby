package com.rouby.user.infrastructure.token;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AesHmacVerificationTokenProvider {

  private static final String TOKEN_PREFIX = "EmailVerification ";

  @Value("${verification-token.aes-secret}")
  private String aesKeyRaw;
  @Value("${verification-token.hmac-secret}")
  private String hmacKey;
  @Value("${verification-token.expiration}")
  private long expirationMillis;

  private byte[] aesKey;

  @PostConstruct
  private void initKey() {
    byte[] keyBytes = aesKeyRaw.getBytes(StandardCharsets.UTF_8);
    if (keyBytes.length < 16) {
      throw new IllegalStateException("AES 키는 최소 16바이트 이상이어야 합니다.");
    }
    aesKey = Arrays.copyOfRange(keyBytes, 0, 16);
  }

  public String createVerificationToken(String email) {
    VerificationPayload payload = new VerificationPayload(email,
        System.currentTimeMillis() + expirationMillis);
    byte[] encrypted = encrypt(payload.serialize());
    byte[] signature = sign(encrypted);
    return encode(encrypted) + "." + encode(signature);
  }

  public boolean validateVerificationToken(String token) {
    log.info(token);
    TokenParts parts = parseToken(token);
    if (parts == null || !verify(parts.encrypted(), parts.signature())) {
      log.info("token is null or invalid signature: {}",token);
      return false;
    }

    VerificationPayload payload = deserializeSafely(parts.encrypted());
    log.info("payload: {}",payload);
    return payload != null && payload.exp() > System.currentTimeMillis();
  }

  public String extractEmail(String token) {
    TokenParts parts = parseToken(token);
    if (parts == null || !verify(parts.encrypted(), parts.signature())) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    VerificationPayload payload = deserializeSafely(parts.encrypted());
    if (payload == null) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    return payload.email();
  }

  private record VerificationPayload(String email, long exp) {

    public String serialize() {
      return email + ":" + exp;
    }

    public static VerificationPayload deserialize(String input) {
      String[] parts = input.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("유효하지 않은 페이로드 형식입니다.");
      }
      return new VerificationPayload(parts[0], Long.parseLong(parts[1]));
    }
  }

  private record TokenParts(byte[] encrypted, byte[] signature) {

  }

  private VerificationPayload deserializeSafely(byte[] encrypted) {
    try {
      String plain = decrypt(encrypted);
      return VerificationPayload.deserialize(plain);
    } catch (Exception e) {
      return null;
    }
  }

  private byte[] encrypt(String plainText) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException("암호화 실패", e);
    }
  }

  private String decrypt(byte[] encrypted) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] decrypted = cipher.doFinal(encrypted);
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("복호화 실패", e);
    }
  }

  private byte[] sign(byte[] data) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(hmacKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      return mac.doFinal(data);
    } catch (Exception e) {
      throw new RuntimeException("서명 생성 실패", e);
    }
  }

  private boolean verify(byte[] data, byte[] expectedSignature) {
    byte[] actual = sign(data);
    return MessageDigest.isEqual(expectedSignature, actual);
  }

  private TokenParts parseToken(String token) {
    if (!token.startsWith(TOKEN_PREFIX)) return null;
    String rawToken = token.substring(TOKEN_PREFIX.length());

    String[] parts = rawToken.split("\\.");
    if (parts.length != 2) {
      return null;
    }

    try {
      byte[] encrypted = Base64.getDecoder().decode(parts[0]);
      byte[] signature = Base64.getDecoder().decode(parts[1]);
      return new TokenParts(encrypted, signature);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  private String encode(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }
}
