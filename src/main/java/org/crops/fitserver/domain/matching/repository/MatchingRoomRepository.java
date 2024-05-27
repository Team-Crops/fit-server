package org.crops.fitserver.domain.matching.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {

  @Query("select mr from MatchingRoom mr "
      + "join fetch mr.matchingList ml "
      + "join fetch ml.user u "
      + "join fetch u.userInfo ui "
      + "where mr.isCompleted = false "
      + "and mr.createdAt >= :expiredAt "
      + "and mr.isDeleted = false")
  List<MatchingRoom> findMatchingRoomNotComplete(LocalDateTime expiredAt);

  @Query("select mr from MatchingRoom mr "
      + "join fetch mr.matchingList ml "
      + "join fetch ml.user u "
      + "join fetch u.userInfo ui "
      + "where mr.id = :id")
  Optional<MatchingRoom> findWithMatchingMembersById(Long id);

  boolean existsByChatRoomId(long chatRoomId);
}
