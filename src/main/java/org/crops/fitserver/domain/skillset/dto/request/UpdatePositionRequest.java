package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record UpdatePositionRequest(JsonNullable<@NotBlank String> displayName,
                                    JsonNullable<@NotBlank String> displayNameEn,
                                    JsonNullable<@NotBlank String> imageUrl) {

}
