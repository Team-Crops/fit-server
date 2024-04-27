package org.crops.fitserver.domain.user.repository;

import java.util.Optional;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

  @Query("select ub from UserBlock ub "
      + "where ub.user = :user "
      + "AND ub.blockStatus = 'BLOCKED' "
      + "and ub.unblockedAt > current_timestamp() "
      + "order by ub.unblockedAt desc "
      + "limit 1")
  Optional<UserBlock> findActiveBlock(User user);
}
