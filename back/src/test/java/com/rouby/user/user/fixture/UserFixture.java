package com.rouby.user.user.fixture;

import com.rouby.user.user.domain.entity.User;

public class UserFixture {

  public static User create(int i) {

    return User.builder()
        .email("test" + i + "@test.com")
        .nickname("test" + i)
        .password("test" + i)
        .notificationEnabled(Boolean.TRUE)
        .build();
  }
}
