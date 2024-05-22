package org.crops.fitserver.domain.matching.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReadyMatchingRequest(
        @NotNull boolean isReady
) {

}
