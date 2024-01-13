package org.crops.fitserver.domain.skillSet.repository;

import java.util.Optional;
import org.crops.fitserver.domain.skillSet.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

  Optional<Skill> findByDisplayName(String displayName);
}
