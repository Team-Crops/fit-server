package org.crops.fitserver.domain.project.repository;

import org.crops.fitserver.domain.project.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

}
