package org.crops.fitserver.domain.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;

public record UpdatePolicyAgreementRequest(@Valid @Size(min = 1) List<PolicyAgreementDto> policyAgreementList) {

}
