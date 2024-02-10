package org.crops.fitserver.domain.user.dto.response;

import java.util.List;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;

public record GetPolicyAgreementResponse(
    List<PolicyAgreementDto> policyAgreementList
) {
  public static GetPolicyAgreementResponse of(List<PolicyAgreementDto> policyAgreementDtoList) {
    return new GetPolicyAgreementResponse(policyAgreementDtoList);
  }
}
