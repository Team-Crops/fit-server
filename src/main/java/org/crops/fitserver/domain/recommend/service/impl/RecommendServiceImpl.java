package org.crops.fitserver.domain.recommend.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.recommend.domain.UserLikes;
import org.crops.fitserver.domain.recommend.repository.UserLikesRepository;
import org.crops.fitserver.domain.recommend.service.RecommendService;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.Users;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.cache.CacheRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

  private final UserLikesRepository userLikesRepository;
  private final UserRepository userRepository;
  private final CacheRepository cacheRepository;

  private static final String RANDOM_SEED_KEY = "recommend:randomSeed:";

  @Override
  public Users recommendUser(
      long userId,
      Boolean liked,
      List<Long> positionIds,
      List<Long> skillIds,
      BackgroundStatus backgroundStatus,
      Long regionId,
      Integer projectCount,
      List<Short> activityHour,
      int page,
      int size,
      int randomSeed) {
    return Users.of(
        userRepository.findAllByFilter(
            userId,
            liked,
            positionIds,
            skillIds,
            backgroundStatus,
            regionId,
            projectCount,
            activityHour,
            page,
            size,
            randomSeed
        ));
  }

  @Override
  public boolean isLikeUser(long likeUserId, long likedUserId) {
    return userLikesRepository.existsByLikeUserIdAndLikedUserId(likeUserId, likedUserId);
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

  @Override
  public int getRandomSeed(long userId, int page) {
    var key = RANDOM_SEED_KEY + userId;
    if (page == 0) {
      var randomSeed = newRandomSeed();
      cacheRepository.set(key, randomSeed, 1, TimeUnit.HOURS);
    }

    return cacheRepository.get(RANDOM_SEED_KEY + userId, Integer.class,this::newRandomSeed);
  }

  private int newRandomSeed() {
    return (int) (Math.random() * 10);
  }
}
