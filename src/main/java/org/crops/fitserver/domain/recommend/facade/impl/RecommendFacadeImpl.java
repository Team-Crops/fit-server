package org.crops.fitserver.domain.recommend.facade.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.Users;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecommendFacadeImpl implements RecommendFacade {

  private static final int DEFAULT_PAGE_SIZE = 10;
  private final RecommendService recommendService;

  @Override
  @Transactional(readOnly = true)
  public List<RecommendUserDto> recommendUser(long userId, RecommendUserRequest request) {
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
        DEFAULT_PAGE_SIZE
    );
    return recommendedUsers
        .getUsers()
        .stream()
        .map(recommendUser ->
            RecommendUserDto.of(
                recommendUser,
                getLiked(userId, recommendUser)
            ))
        .toList();
  }

  private boolean getLiked(long userId, User recommendUser) {
    return recommendUser
        .getLikedUsers()
        .stream()
        .anyMatch(userLikes -> userLikes.getLikeUser().getId().equals(userId));
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
