package org.crops.fitserver.domain.project.repository;

import java.util.List;
import org.crops.fitserver.domain.project.domain.ProjectReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectReportHistoryRepository extends JpaRepository<ProjectReportHistory, Long> {

  Integer countByTargetUserId(Long targetUserId);

  List<ProjectReportHistory> findAllByTargetUserId(Long targetUserId);
}
