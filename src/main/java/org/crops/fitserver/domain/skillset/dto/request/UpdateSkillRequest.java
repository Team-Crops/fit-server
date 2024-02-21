package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateSkillRequest(JsonNullable<@NotBlank String> displayName) {

}
