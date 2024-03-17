package org.crops.fitserver.domain.recommend.dto.request;

import java.util.List;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;

public record RecommendUserRequest(
    List<Long> positionId,
    Long skillId,
    Long regionId,
    Integer projectCount,
    BackgroundStatus backgroundStatus,
    Short activityHour,
    Boolean likeUser
) {

}
