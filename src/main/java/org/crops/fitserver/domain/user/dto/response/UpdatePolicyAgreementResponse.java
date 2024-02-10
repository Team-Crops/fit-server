package org.crops.fitserver.domain.user.dto.response;

import java.util.List;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;

public record UpdatePolicyAgreementResponse(List<PolicyAgreementDto> policyAgreementList) {
  public static UpdatePolicyAgreementResponse of(List<PolicyAgreementDto> policyAgreementDtoList) {
    return new UpdatePolicyAgreementResponse(policyAgreementDtoList);
  }
}
