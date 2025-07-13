package com.rouby.user.domain.service;

public interface UserPasswordEncoder {

  String encode(String rawPassword);

  boolean matches(String rawPassword, String encodedPassword);
}