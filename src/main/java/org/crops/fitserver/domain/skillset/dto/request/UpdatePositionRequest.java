package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePositionRequest (@NotBlank String displayName) {
}
