package com.rouby.common.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCryptoUtils {
  private static final int IV_LENGTH = 16;
  private static final int KEY_LENGTH = 32;
  private final SecureRandom secureRandom = new SecureRandom();
  private final byte[] aesKey;

  public AesCryptoUtils (String aesKeyRaw) {
    byte[] keyBytes = aesKeyRaw.getBytes(UTF_8);
    if (keyBytes.length < KEY_LENGTH) {
      throw new IllegalStateException(String.format(
          "AES 키는 최소 %d 바이트 이상이어야 합니다.", KEY_LENGTH
      ));
    }
    this.aesKey = Arrays.copyOf(keyBytes, IV_LENGTH);
  }


  public byte[] encrypt(String plaintext) throws Exception {
    byte[] iv = new byte[IV_LENGTH];
    secureRandom.nextBytes(iv);
    Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, iv);
    byte[] ciphertext = cipher.doFinal(plaintext.getBytes(UTF_8));
    return ByteBuffer.allocate(iv.length + ciphertext.length).put(iv).put(ciphertext).array();
  }

  public String decrypt(byte[] encrypted) throws Exception {
    ByteBuffer buffer = ByteBuffer.wrap(encrypted);
    byte[] iv = new byte[IV_LENGTH];
    buffer.get(iv);
    byte[] ciphertext = new byte[buffer.remaining()];
    buffer.get(ciphertext);
    Cipher cipher = initCipher(Cipher.DECRYPT_MODE, iv);
    byte[] plain = cipher.doFinal(ciphertext);
    return new String(plain, UTF_8);
  }

  private Cipher initCipher(int mode, byte[] iv) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(mode, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(iv));
    return cipher;
  }
}
