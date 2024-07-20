package org.crops.fitserver.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdatePolicyAgreementRequest;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.dto.request.WithdrawRequest;
import org.crops.fitserver.domain.user.dto.response.GetPolicyAgreementResponse;
import org.crops.fitserver.domain.user.dto.response.GetUserStatusAndInfoResponse;
import org.crops.fitserver.domain.user.dto.response.UpdatePolicyAgreementResponse;
import org.crops.fitserver.domain.user.facade.UserFacade;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/{userId}")
  public ResponseEntity<GetUserStatusAndInfoResponse> getUserStatusAndInfoById(
      @CurrentUserId Long loginUserId,
      @PathVariable(name = "userId") Long userId) {
    return ResponseEntity.ok(
        userFacade.getUserStatusAndInfo(loginUserId, userId));
  }

  @PatchMapping()
  public ResponseEntity<UserInfoDto> updateUser(
      @CurrentUserId Long userId,
      @Valid @RequestBody UpdateUserRequest updateUserRequest) {
    return ResponseEntity.ok(userFacade.updateUserWithInfo(userId, updateUserRequest));
  }

  @GetMapping("/policy-agreement")
  public ResponseEntity<GetPolicyAgreementResponse> getPolicyAgreement(
      @CurrentUserId Long userId) {
    return ResponseEntity.ok(
        GetPolicyAgreementResponse.of(userFacade.getPolicyAgreementList(userId)));
  }

  @PutMapping("/policy-agreement")
  public ResponseEntity<UpdatePolicyAgreementResponse> updatePolicyAgreement(
      @CurrentUserId Long userId,
      @Valid @RequestBody UpdatePolicyAgreementRequest updatePolicyAgreementRequest) {
    if (updatePolicyAgreementRequest.policyAgreementList().stream()
        .map(PolicyAgreementDto::policyType).distinct().count()
        != updatePolicyAgreementRequest.policyAgreementList().size()) {
      throw new BusinessException(ErrorCode.DUPLICATED_POLICY_REQUEST_EXCEPTION);
    }

    return ResponseEntity.ok(
        UpdatePolicyAgreementResponse.of(userFacade.updatePolicyAgreement(userId,
            updatePolicyAgreementRequest.policyAgreementList())));
  }

  @PostMapping("/withdraw")
  public ResponseEntity<Void> withdraw(
      @Valid @RequestBody WithdrawRequest request,
      @CurrentUserId long userId) {
    userFacade.withdraw(userId, request.withdrawReason(), request.isAgree());
    return ResponseEntity.noContent().build();
  }
}
