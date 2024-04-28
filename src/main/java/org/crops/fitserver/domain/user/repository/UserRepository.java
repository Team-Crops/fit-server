package org.crops.fitserver.domain.user.repository;

import java.util.Optional;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends
    JpaRepository<User, Long>,
    UserRepositoryCustom {

  Optional<User> findById(Long userId);

  @Query("select u from User u "
      + "left join u.userInfo ui "
      + "left join ui.userInfoSkills uis "
      + "where u.id = :userId")
  Optional<User> findWithInfo(Long userId);

  @Query("select u from User u "
      + "left join fetch u.userInfo ui "
      + "left join fetch u.likeUsers lu "
      + "where u.id = :userId")
  Optional<User> findWithLikeUsers(Long userId);
}
