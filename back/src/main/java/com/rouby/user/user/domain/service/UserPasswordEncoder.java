package com.rouby.user.user.domain.service;

public interface UserPasswordEncoder {

  String encode(String rawPassword);

  boolean matches(String rawPassword, String encodedPassword);
}