package org.crops.fitserver.domain.project.repository;

import java.util.List;
import org.crops.fitserver.domain.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query("select p from Project p "
      + "join p.projectMemberList pm "
      + "where pm.user.id = :userId")
  List<Project> findAllByUserId(Long userId);

  boolean existsByChatRoomId(long chatRoomId);
}
