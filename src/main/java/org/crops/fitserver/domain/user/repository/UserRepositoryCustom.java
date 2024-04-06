package org.crops.fitserver.domain.user.repository;

import java.util.List;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.User;

public interface UserRepositoryCustom {

  List<User> findAllByFilter(
      Long userId,
      Boolean liked,
      List<Long> positionId,
      List<Long> skillId,
      BackgroundStatus backgroundStatus,
      Long regionId,
      Integer projectCount,
      List<Short> activityHour,
      Integer page,
      Integer size
  );
}