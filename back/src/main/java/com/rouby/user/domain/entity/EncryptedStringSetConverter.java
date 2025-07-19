package com.rouby.user.domain.entity;

import com.rouby.common.utils.AesCryptoUtils;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Converter
public class EncryptedStringSetConverter implements AttributeConverter<Set<String>, String> {

  private static final Base64.Encoder ENCODER = Base64.getEncoder();
  private static final Base64.Decoder DECODER = Base64.getDecoder();

  @Value("${db.aes-secret}")
  private String aesKeyRaw;

  private AesCryptoUtils cryptoUtils;

  @PostConstruct
  private void init() {
    this.cryptoUtils = new AesCryptoUtils(aesKeyRaw);
  }

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return null;
    }
    try {
      String joined = String.join(",", attribute); // 예: "A,B,C"
      byte[] encrypted = cryptoUtils.encrypt(joined);
      return ENCODER.encodeToString(encrypted);
    } catch (Exception e) {
      throw new IllegalStateException("Set<String> 암호화 중 오류 발생", e);
    }
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return Collections.emptySet();
    }
    try {
      byte[] decoded = DECODER.decode(dbData);
      String decrypted = cryptoUtils.decrypt(decoded);
      return Arrays.stream(decrypted.split(","))
          .map(String::trim)
          .collect(Collectors.toSet());
    } catch (Exception e) {
      throw new IllegalStateException("암호화된 문자열을 Set<String>으로 복호화 중 오류 발생", e);
    }
  }
}


