package org.crops.fitserver.domain.recommend.service;

import java.util.List;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.Users;

public interface RecommendService {

  Users recommendUser(
      long userId,
      Boolean liked,
      List<Long> positionId,
      List<Long> skillId,
      BackgroundStatus backgroundStatus,
      Long regionId,
      Integer projectCount,
      List<Short> activityHour,
      int page,
      int size,
      int randomSeed);

  void likeUser(long likeUserId, long likedUserId);

  void unlikeUser(long likeUserId, long likedUserId);

  int getRandomSeed(long userId, int page);
}
