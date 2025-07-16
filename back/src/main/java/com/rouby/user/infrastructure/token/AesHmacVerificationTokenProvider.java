package com.rouby.user.infrastructure.token;

import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AesHmacVerificationTokenProvider {

  private static final String TOKEN_PREFIX = "EmailVerification ";
  private static final int IV_LENGTH = 16;
  private static final Base64.Encoder ENCODER = Base64.getEncoder();
  private static final Base64.Decoder DECODER = Base64.getDecoder();

  @Value("${verification-token.aes-secret}")
  private String aesKeyRaw;
  @Value("${verification-token.hmac-secret}")
  private String hmacKeyRaw;
  @Value("${verification-token.expiration}")
  private long expirationMillis;

  private byte[] aesKey;
  private byte[] hmacKey;
  private final SecureRandom secureRandom = new SecureRandom();

  @PostConstruct
  private void initKey() {
    byte[] keyBytes = aesKeyRaw.getBytes(UTF_8);
    if (keyBytes.length < IV_LENGTH) {
      throw new IllegalStateException("AES 키는 최소 16바이트 이상이어야 합니다.");
    }
    aesKey = Arrays.copyOf(keyBytes, IV_LENGTH);
    hmacKey = hmacKeyRaw.getBytes(UTF_8);
  }

  public String createVerificationToken(String email) {
    if (email == null) {
      throw new IllegalArgumentException("이메일은 null이 될 수 없습니다.");
    }
    VerificationPayload payload = new VerificationPayload(email,
        System.currentTimeMillis() + expirationMillis);
    byte[] encrypted = encrypt(payload.serialize());
    byte[] signature = sign(encrypted);
    return TOKEN_PREFIX + encode(encrypted) + "." + encode(signature);
  }

  public boolean validateVerificationToken(String token) {
    TokenParts parts = parse(token);
    if (parts == null || !verify(parts.encrypted(), parts.signature())) {
      return false;
    }

    VerificationPayload payload = deserializeSafely(parts.encrypted());
    return payload != null && payload.exp() > System.currentTimeMillis();
  }

  public String extractEmail(String token) {
    TokenParts parts = parse(token);
    if (parts == null || !verify(parts.encrypted(), parts.signature())) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    VerificationPayload payload = deserializeSafely(parts.encrypted());
    if (payload == null) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    return payload.email();
  }

  private VerificationPayload deserializeSafely(byte[] encrypted) {
    try {
      String plain = decrypt(encrypted);
      return VerificationPayload.deserialize(plain);
    } catch (Exception e) {
      log.debug("토큰 역직렬화 실패: {}", e.getMessage());
      return null;
    }
  }

  private byte[] encrypt(String plaintext) {
    try {
      byte[] iv = new byte[IV_LENGTH];
      secureRandom.nextBytes(iv);
      Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, iv);
      byte[] ciphertext = cipher.doFinal(plaintext.getBytes(UTF_8));
      return ByteBuffer.allocate(iv.length + ciphertext.length)
          .put(iv)
          .put(ciphertext)
          .array();
    } catch (Exception e) {
      log.error("암호화 실패: {}", e.getMessage());
      throw new RuntimeException("암호화 실패", e);
    }
  }

  private String decrypt(byte[] data) {
    try {
      ByteBuffer buffer = ByteBuffer.wrap(data);
      byte[] iv = new byte[IV_LENGTH];
      buffer.get(iv);
      byte[] ciphertext = new byte[buffer.remaining()];
      buffer.get(ciphertext);
      Cipher cipher = initCipher(Cipher.DECRYPT_MODE, iv);
      byte[] plaintext = cipher.doFinal(ciphertext);
      return new String(plaintext, UTF_8);
    } catch (Exception e) {
      log.error("복호화 실패: {}", e.getMessage());
      throw new RuntimeException("복호화 실패", e);
    }
  }

  private Cipher initCipher(int mode, byte[] iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(mode,
          new SecretKeySpec(aesKey, "AES"),
          new IvParameterSpec(iv));
      return cipher;
    } catch (Exception e) {
      log.error("Cipher 초기화 실패: {}", e.getMessage());
      throw new RuntimeException("Cipher 초기화 실패", e);
    }
  }

  private byte[] sign(byte[] data) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
      return mac.doFinal(data);
    } catch (Exception e) {
      log.error("서명 생성 실패: {}", e.getMessage());
      throw new RuntimeException("서명 생성 실패", e);
    }
  }

  private boolean verify(byte[] data, byte[] expectedSignature) {
    byte[] actual = sign(data);
    return MessageDigest.isEqual(expectedSignature, actual);
  }

  private TokenParts parse(String token) {
    if (token == null || !token.startsWith(TOKEN_PREFIX)) return null;
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

  private record TokenParts(byte[] encrypted, byte[] signature) {}
}
