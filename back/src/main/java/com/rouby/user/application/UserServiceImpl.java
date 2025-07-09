package com.rouby.user.application;

import com.rouby.common.exception.CustomException;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;

  //Auth에서 UserService 참조하기로해서 만들어 보았는데 맞는지 잘 모르겠습니다ㅜ 피드백 주시면 수정할게용!
  @Transactional(readOnly = true)
  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> CustomException.from(UserErrorCode.INVALID_USER));
  }
}
