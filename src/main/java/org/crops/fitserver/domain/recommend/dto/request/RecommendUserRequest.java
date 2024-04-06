package org.crops.fitserver.domain.recommend.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;

public record RecommendUserRequest(
    Boolean liked,
    List<Long> positionId,
    List<Long> skillId,
    BackgroundStatus backgroundStatus,
    Long regionId,
    Integer projectCount,
    List<Short> activityHour,
    @NotNull Integer page
) {

}
