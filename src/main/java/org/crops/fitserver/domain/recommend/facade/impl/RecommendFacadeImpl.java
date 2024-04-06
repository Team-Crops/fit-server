package org.crops.fitserver.domain.recommend.facade.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.domain.Users;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecommendFacadeImpl implements RecommendFacade {

  private static final Integer DEFAULT_PAGE_SIZE = 10;
  private final RecommendService recommendService;

  @Override
  @Transactional(readOnly = true)
  public List<RecommendUserDto> recommendUser(Long userId, RecommendUserRequest request) {
    Users users = recommendService.recommendUser(
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
    return users
        .getUsers()
        .stream()
        .map(recommendUser ->
            RecommendUserDto.of(
                recommendUser,
                recommendService.isLiked(userId, recommendUser.getId())))
        .toList();
  }

  @Override
  public void likeUser(Long likeUserId, Long likedUserId, Boolean like) {
    if (like) {
      recommendService.likeUser(likeUserId, likedUserId);
    }
    if (!like) {
      recommendService.unlikeUser(likeUserId, likedUserId);
    }
  }
}
