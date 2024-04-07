package org.crops.fitserver.domain.recommend.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.recommend.domain.UserLikes;
import org.crops.fitserver.domain.recommend.repository.UserLikesRepository;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.Users;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

  private final UserLikesRepository userLikesRepository;
  private final UserRepository userRepository;

  @Override
  public Users recommendUser(
      long userId,
      Boolean liked,
      List<Long> positionId,
      List<Long> skillId,
      BackgroundStatus backgroundStatus,
      Long regionId,
      Integer projectCount,
      List<Short> activityHour,
      Integer page,
      Integer size) {
    return Users.of(
        userRepository.findAllByFilter(
            userId,
            liked,
            positionId,
            skillId,
            backgroundStatus,
            regionId,
            projectCount,
            activityHour,
            page,
            size
        ));
  }

  @Override
  public boolean isLiked(long likeUserId, long likedUserId) {
    User likeUser = userRepository.findById(likeUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    User likedUser = userRepository.findById(likedUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    return userLikesRepository.existsByLikeUserAndLikedUser(likeUser, likedUser);
  }

  @Override
  public void likeUser(long likeUserId, long likedUserId) {
    User likeUser = userRepository.findById(likeUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    User likedUser = userRepository.findById(likedUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    if (userLikesRepository.existsByLikeUserAndLikedUser(likeUser, likedUser)) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }
    userLikesRepository.save(UserLikes.of(likeUser, likedUser));
  }

  @Override
  public void unlikeUser(long likeUserId, long likedUserId) {
    User likeUser = userRepository.findById(likeUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    User likedUser = userRepository.findById(likedUserId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));

    UserLikes userLikes = userLikesRepository.findByLikeUserAndLikedUser(likeUser, likedUser)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    userLikesRepository.delete(userLikes);
  }
}
