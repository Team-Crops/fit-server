package org.crops.fitserver.domain.recommend.repository;

import java.util.Optional;
import org.crops.fitserver.domain.recommend.domain.UserLikes;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.repository.Repository;

public interface UserLikesRepository extends Repository<UserLikes, Long> {

  boolean existsByLikeUserAndLikedUser(User likeUser, User likedUser);

  UserLikes save(UserLikes userLikes);

  void delete(UserLikes userLikes);

  Optional<UserLikes> findByLikeUserAndLikedUser(User likeUser, User likedUser);
}
