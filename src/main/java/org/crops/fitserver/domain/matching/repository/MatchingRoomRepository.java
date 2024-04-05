package org.crops.fitserver.domain.matching.repository;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {

  @Query("select mr from MatchingRoom mr "
      + "where mr.isCompleted = false "
      + "and mr.isDeleted = false")
  List<MatchingRoom> findMatchingRoomNotComplete();

  @Query("select mr from MatchingRoom mr "
      + "join fetch mr.matchingList ml "
      + "join fetch ml.user u "
      + "where mr.id = :id")
  Optional<MatchingRoom> findWithMatchingMembersById(Long id);
}
