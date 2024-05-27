package org.crops.fitserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record WithdrawRequest(
    String withdrawReason,
    @NotNull boolean isAgree
) {
}
