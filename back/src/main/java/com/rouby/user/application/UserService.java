package com.rouby.user.application;

import com.rouby.user.domain.entity.User;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 08.
 */

public interface UserService {

  public User findByEmail(String email);
}
