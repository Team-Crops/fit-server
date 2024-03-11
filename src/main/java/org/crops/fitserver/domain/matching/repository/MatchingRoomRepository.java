package org.crops.fitserver.domain.matching.repository;

import java.util.List;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {

  @Query("select mr from MatchingRoom mr "
      + "where mr.isComplete = false "
      + "and mr.isDeleted = false")
  List<MatchingRoom> findMatchingRoomNotComplete();
}
