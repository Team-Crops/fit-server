package org.crops.fitserver.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record SocialLoginRequest(
    @NotNull String code
) {
}