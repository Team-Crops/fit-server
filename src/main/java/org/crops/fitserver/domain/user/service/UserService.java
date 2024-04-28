package org.crops.fitserver.domain.user.service;

import java.util.List;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;

public interface UserService {

  User getById(Long userId);

  User getUserWithInfo(Long userId);

  User getUserWithLikeUsers(Long userId);

  User updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest);

  List<UserPolicyAgreement> getPolicyAgreementList(Long userId);

  List<UserPolicyAgreement> updatePolicyAgreement(Long userId, List<PolicyAgreementDto> policyAgreementDtoList);
}
