package org.crops.fitserver.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.facade.UserFacade;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.crops.fitserver.global.security.PrincipalDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @GetMapping()
  public ResponseEntity<UserInfoDto> getUser(
      @CurrentUserId Long userId) {
    return ResponseEntity.ok(userFacade.getUserWithInfo(userId));
  }

  @PutMapping()
  public ResponseEntity<UserInfoDto> updateUser(
      @CurrentUserId Long userId,
      UpdateUserRequest updateUserRequest) {
    return ResponseEntity.ok(userFacade.updateUserWithInfo(userId, updateUserRequest));
  }
}
