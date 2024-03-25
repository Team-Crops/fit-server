package org.crops.fitserver.domain.project.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.project.constant.ProjectStatus;

@Builder
public record ProjectDto(
    Long projectId,
    String projectName,
    List<ProjectMemberDto> projectMemberList,
    Long chatRoomId,
    LocalDateTime createdAt,
    LocalDateTime completedAt,
    ProjectStatus projectStatus
) {

}
