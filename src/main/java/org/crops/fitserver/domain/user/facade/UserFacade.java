package org.crops.fitserver.domain.user.facade;

import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;

public interface UserFacade {

  public UserInfoDto getUserWithInfo(Long userId);

  public UserInfoDto updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest);
}
