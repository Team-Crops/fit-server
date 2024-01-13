package org.crops.fitserver.domain.skillset.repository;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PositionRepository extends JpaRepository<Position, Long> {

  Optional<Position> findByDisplayName(String displayName);

  @Query("select p.skills from Position p join fetch p.skills where p.id = :positionId")
  List<Skill> findByPositionId(Long positionId);
}
