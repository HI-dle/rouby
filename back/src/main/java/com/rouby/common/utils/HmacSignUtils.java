package com.rouby.common.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSignUtils {
  private final byte[] hmacKey;
  private static final int MIN_KEY_LENGTH = 32;

  public HmacSignUtils(String hmacKeyRaw) {
    if (hmacKeyRaw == null || hmacKeyRaw.getBytes(UTF_8).length < MIN_KEY_LENGTH) {
        throw new IllegalArgumentException(String.format(
            "HMAC 키는 최소 %d 바이트 이상이어야 합니다.", MIN_KEY_LENGTH
        ));
    }
    this.hmacKey = hmacKeyRaw.getBytes(UTF_8);
  }

  public byte[] sign(byte[] data) throws Exception {

    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
    return mac.doFinal(data);
  }

  public boolean verify(byte[] data, byte[] expectedSignature) throws Exception {
    byte[] actual = sign(data);
    return MessageDigest.isEqual(expectedSignature, actual);
  }
}