package org.crops.fitserver.domain.user.facade;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade{
  private final UserService userService;

  @Override
  public UserInfoDto getUserWithInfo(Long userId) {
    return UserInfoDto.from(userService.getUserWithInfo(userId));
  }

  @Override
  public UserInfoDto updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest) {
    return UserInfoDto.from(userService.updateUserWithInfo(userId, updateUserRequest));
  }
}
