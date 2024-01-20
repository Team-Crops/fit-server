package org.crops.fitserver.domain.region.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record CreateRegionRequest(@NotBlank String displayName) {

}
