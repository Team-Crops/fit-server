package org.crops.fitserver.domain.matching.repository;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

  Optional<Matching> findMatchingByUserAndStatusIn(User user, List<MatchingStatus> statusList);

  @Query("select m from Matching m "
      + "where m.expiredAt <= current_timestamp "
      + "and m.isDeleted = false")
  List<Matching> findExpireMatching();

  @Query("select m from Matching m "
      + "where m.status = 'WAITING'"
      + "and m.matchingRoom is null "
      + "and m.isDeleted = false")
  List<Matching> findMatchingWithoutRoom();
}
