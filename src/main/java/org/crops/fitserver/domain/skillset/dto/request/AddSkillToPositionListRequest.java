package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;

@Builder
public record AddSkillToPositionListRequest(@NotEmpty List<Long> positionIds) {

}
