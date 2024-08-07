package org.crops.fitserver.domain.user.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.dto.response.GetUserStatusAndInfoResponse;
import org.crops.fitserver.domain.user.service.UserService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final UserService userService;
  private final RecommendService recommendService;
  private final MatchingRepository matchingRepository;

  @Override
  public UserInfoDto getUserWithInfo(Long userId) {
    return UserInfoDto.from(userService.getUserWithInfo(userId));
  }

  @Override
  @Transactional(readOnly = true)
  public GetUserStatusAndInfoResponse getUserStatusAndInfo(Long loginUserId, Long userId) {
    var userWithInfo = userService.getUserWithInfo(userId);
    var likeUser = recommendService.isLikeUser(loginUserId, userId);
    return GetUserStatusAndInfoResponse.of(userWithInfo, likeUser);
  }

  @Override
  public UserInfoDto updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest) {
    return UserInfoDto.from(userService.updateUserWithInfo(userId, updateUserRequest));
  }

  @Override
  public List<PolicyAgreementDto> getPolicyAgreementList(Long userId) {
    return userService.getPolicyAgreementList(userId).stream()
        .map(PolicyAgreementDto::from)
        .toList();
  }

  @Override
  public List<PolicyAgreementDto> updatePolicyAgreement(Long userId,
      List<PolicyAgreementDto> policyAgreementDtoList) {
    return userService.updatePolicyAgreement(userId, policyAgreementDtoList)
        .stream()
        .map(PolicyAgreementDto::from)
        .toList();
  }

  @Override
  @Transactional
  public void withdraw(long userId, String withdrawReason, boolean isAgree) {
    if (!isAgree) {
      throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }
    var user = userService.getUserWithInfo(userId);
    userService.withdraw(user, withdrawReason, isAgree);
  }
}
