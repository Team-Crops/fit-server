package org.crops.fitserver.domain.user.service;

import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;

public interface UserService {

  User getUserWithInfo(Long userId);

  User updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest);
}
