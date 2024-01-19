package org.crops.fitserver.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.facade.UserFacade;
import org.crops.fitserver.global.annotation.V1;
import org.crops.fitserver.global.security.PrincipalDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@V1
@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @GetMapping()
  public ResponseEntity<UserInfoDto> getUser(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    var userId = principalDetails.getUserId();
    return ResponseEntity.ok(userFacade.getUserWithInfo(userId));
  }

  @PutMapping()
  public ResponseEntity<UserInfoDto> updateUser(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      UpdateUserRequest updateUserRequest) {
    var userId = principalDetails.getUserId();
    return ResponseEntity.ok(userFacade.updateUserWithInfo(userId, updateUserRequest));
  }
}
