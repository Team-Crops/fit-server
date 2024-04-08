package org.crops.fitserver.domain.recommend.facade.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.Users;
import org.crops.fitserver.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecommendFacadeImpl implements RecommendFacade {

  private static final int DEFAULT_PAGE_SIZE = 10;
  private final UserService userService;
  private final RecommendService recommendService;

  @Override
  @Transactional(readOnly = true)
  public List<RecommendUserDto> recommendUser(long userId,RecommendUserRequest request) {
    int randomSeed = (int) (Math.random() * 10);
    User user = userService.getUserWithLikeUsers(userId);
    Users recommendedUsers = recommendService.recommendUser(
        userId,
        request.liked(),
        request.positionId(),
        request.skillId(),
        request.backgroundStatus(),
        request.regionId(),
        request.projectCount(),
        request.activityHour(),
        request.page(),
        DEFAULT_PAGE_SIZE,
        randomSeed
    );
    return recommendedUsers
        .getUsers()
        .stream()
        .map(recommendUser ->
            RecommendUserDto.of(
                recommendUser,
                getLiked(user, recommendUser.getId())
            ))
        .toList();
  }

  private boolean getLiked(User user, long recommendUserId) {
    return user
        .getLikeUsers()
        .stream()
        .anyMatch(userLikes -> userLikes.getLikedUser().getId().equals(recommendUserId));
  }

  @Override
  public void likeUser(long likeUserId, long likedUserId, boolean like) {
    if (like) {
      recommendService.likeUser(likeUserId, likedUserId);
    } else {
      recommendService.unlikeUser(likeUserId, likedUserId);
    }
  }
}
