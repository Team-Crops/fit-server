package org.crops.fitserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User getUserWithInfo(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  public User updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    user.updateUser(updateUserRequest);
    return user;
  }
}
