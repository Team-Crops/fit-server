package org.crops.fitserver.domain.recommend.repository;

import java.util.Optional;
import org.crops.fitserver.domain.recommend.domain.UserLikes;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {

  boolean existsByLikeUserAndLikedUser(User likeUser, User likedUser);

  void delete(UserLikes userLikes);

  Optional<UserLikes> findByLikeUserAndLikedUser(User likeUser, User likedUser);

  boolean existsByLikeUserIdAndLikedUserId(long likeUserId, long likedUserId);
}
