package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;

@Builder
public record CreatePositionRequest(
    @NotBlank String displayName,
    List<Long> skillIds) {

}