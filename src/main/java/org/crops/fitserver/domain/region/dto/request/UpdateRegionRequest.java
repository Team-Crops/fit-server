package org.crops.fitserver.domain.region.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

public record UpdateRegionRequest(@NotEmpty String displayName) {
}
