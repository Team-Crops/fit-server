package org.crops.fitserver.domain.skillset.repository;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PositionRepository extends JpaRepository<Position, Long> {

  Optional<Position> findByDisplayName(String displayName);

  @Query("select p from Position p left join fetch p.skillSets s")
  List<Position> findAllWithSkills();

  @Query("select p from Position p left join fetch p.skillSets s where p.id = :positionId")
  Optional<Position> findWithSkills(Long positionId);
}
