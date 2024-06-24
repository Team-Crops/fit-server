package org.crops.fitserver.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Builder;
import org.crops.fitserver.domain.user.constant.PolicyType;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;

@Builder
public record PolicyAgreementDto(
    @NotNull PolicyType policyType,
    @NotNull String version,
    @NotNull Boolean isAgree,
    OffsetDateTime updatedAt
) {

  public static PolicyAgreementDto from(UserPolicyAgreement userPolicyAgreement) {
    return PolicyAgreementDto.builder()
        .policyType(userPolicyAgreement.getPolicyType())
        .version(userPolicyAgreement.getVersion())
        .isAgree(userPolicyAgreement.getIsAgree())
        .updatedAt(userPolicyAgreement.getUpdatedAt())
        .build();
  }

  public static PolicyAgreementDto of(
      PolicyType policyType,
      String version,
      Boolean isAgree,
      OffsetDateTime UpdatedAt) {
    return new PolicyAgreementDto(policyType, version, isAgree, UpdatedAt);
  }
}
