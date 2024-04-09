package org.crops.fitserver.domain.project.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.project.constant.ProjectStatus;
import org.crops.fitserver.domain.project.entity.Project;

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

  public static ProjectDto of(Project project, Long userId){
    var owner = project.getProjectMemberList().stream()
        .filter(projectMember -> projectMember.getUser().getId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트에 참여하고 있지 않습니다."));

    return ProjectDto.builder()
        .projectId(project.getId())
        .projectMemberList(project.getProjectMemberList().stream()
            .map(ProjectMemberDto::from)
            .toList())
        .chatRoomId(project.getChatRoomId())
        .projectName(owner.getProjectName())
        .createdAt(owner.getCreatedAt())
        .completedAt(owner.getCompletedAt())
        .projectStatus(owner.getStatus())
        .build();
  }

}
