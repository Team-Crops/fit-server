package org.crops.fitserver.domain.user.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final UserService userService;

  @Override
  public UserInfoDto getUserWithInfo(Long userId) {
    return UserInfoDto.from(userService.getUserWithInfo(userId));
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
  public void deleteUser(long userId) {
    var user = userService.getById(userId);
    userService.deleteUser(user);
  }
}
