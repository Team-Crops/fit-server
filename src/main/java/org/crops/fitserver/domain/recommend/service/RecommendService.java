package org.crops.fitserver.domain.recommend.service;

import java.util.List;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.Users;

public interface RecommendService {

  Users recommendUser(
      Long userId,
      Boolean liked,
      List<Long> positionId,
      List<Long> skillId,
      BackgroundStatus backgroundStatus,
      Long regionId,
      Integer projectCount,
      List<Short> activityHour,
      Integer page,
      Integer size);

  Boolean isLiked(Long userId, Long likeUserId);

  void likeUser(Long likeUserId, Long likedUserId);

  void unlikeUser(Long likeUserId, Long likedUserId);
}
