package org.crops.fitserver.domain.skillset.repository;

import java.util.Optional;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

  boolean existsByDisplayName(String displayName);
}
