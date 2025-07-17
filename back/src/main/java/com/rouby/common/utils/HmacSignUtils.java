package com.rouby.common.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSignUtils {
  private final byte[] hmacKey;

  public HmacSignUtils(String hmacKeyRaw) {
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