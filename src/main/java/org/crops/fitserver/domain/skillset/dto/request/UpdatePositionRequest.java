package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record UpdatePositionRequest (JsonNullable<@NotBlank String> displayName, JsonNullable<@NotBlank String> imageUrl) {
}
