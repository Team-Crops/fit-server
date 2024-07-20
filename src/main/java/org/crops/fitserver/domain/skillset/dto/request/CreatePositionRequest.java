package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.skillset.constant.PositionType;

@Builder
public record CreatePositionRequest(
    @NotBlank String displayName,
    @NotBlank String displayNameEn,
    @NotNull PositionType type,
    List<Long> skillIds,
    @NotBlank String imageUrl) {

}