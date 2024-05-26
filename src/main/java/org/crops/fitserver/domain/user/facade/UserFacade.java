package org.crops.fitserver.domain.user.facade;

import java.util.List;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.dto.response.GetUserStatusAndInfoResponse;

public interface UserFacade {

  UserInfoDto getUserWithInfo(Long userId);

  GetUserStatusAndInfoResponse getUserStatusAndInfo(Long loginUserId, Long userId);

  UserInfoDto updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest);

  List<PolicyAgreementDto> getPolicyAgreementList(Long userId);

  List<PolicyAgreementDto> updatePolicyAgreement(Long userId, List<PolicyAgreementDto> policyAgreementDtoList);

  void deleteUser(long userId);
}