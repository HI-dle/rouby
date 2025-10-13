package com.rouby.assistant.prompt.infrastructure.ai;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class ResponseConverterManager {

  private final Map<Type, BeanOutputConverter<?>> converters = new ConcurrentHashMap<>();

  /**
   * 특정 클래스에 대한 BeanOutputConverter를 조회합니다.
   * 없으면 새로 생성하여 등록하고 반환합니다.
   * * @param targetClass 변환하려는 대상 클래스 (예: MyDto.class)
   * @return 해당 클래스에 맞는 BeanOutputConverter 인스턴스
   */
  @SuppressWarnings("unchecked")
  public <R> BeanOutputConverter<R> getConverter(Class<R> targetClass) {

    BeanOutputConverter<?> converter = converters.computeIfAbsent(
        targetClass,
        type -> new BeanOutputConverter<>((Class<R>) type)
    );
    return (BeanOutputConverter<R>) converter;
  }

  /**
   * List<T> 같은 복합 타입을 위한 컨버터 조회 (Optional)
   * TypeReference를 사용하여 제네릭 정보가 유지되도록 합니다.
   * * @param typeReference 변환하려는 복합 타입 (예: new TypeReference<List<MyDto>>() {})
   * @return 해당 타입에 맞는 BeanOutputConverter 인스턴스
   */
  @SuppressWarnings("unchecked")
  public <R> BeanOutputConverter<R> getConverter(ParameterizedTypeReference<R> typeReference) {

    BeanOutputConverter<?> converter = converters.computeIfAbsent(
        typeReference.getType().getClass(),
        type -> new BeanOutputConverter<>(typeReference)
    );
    return (BeanOutputConverter<R>) converter;
  }
}
