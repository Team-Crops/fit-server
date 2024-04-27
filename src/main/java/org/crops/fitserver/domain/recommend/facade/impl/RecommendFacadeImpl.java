package org.crops.fitserver.domain.recommend.facade.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.Users;
import org.crops.fitserver.domain.user.repository.UserBlockRepository;
import org.crops.fitserver.domain.user.service.UserService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecommendFacadeImpl implements RecommendFacade {

  private final UserService userService;
  private final RecommendService recommendService;
  private final UserBlockRepository userBlockRepository;
  private static final int DEFAULT_PAGE_SIZE = 10;

  @Override
  @Transactional(readOnly = true)
  public List<RecommendUserDto> recommendUser(long userId, RecommendUserRequest request) {
    User user = userService.getUserWithLikeUsers(userId);
    if (userBlockRepository.findActiveBlock(user).isPresent()) {
      throw new BusinessException(ErrorCode.BLOCKED_USER_EXCEPTION);
    }
    int randomSeed = recommendService.getRandomSeed(userId, request.page());
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
