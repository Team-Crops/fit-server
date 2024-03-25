package org.crops.fitserver.domain.matching.dto.request;

import jakarta.validation.constraints.NotNull;

public record ForceOutRequest(
    @NotNull Long userId
) {

}
