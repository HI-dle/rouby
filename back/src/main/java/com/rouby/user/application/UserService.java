package com.rouby.user.application;

import com.rouby.user.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */

public interface UserService {

  public User findByEmail(String email);
}
