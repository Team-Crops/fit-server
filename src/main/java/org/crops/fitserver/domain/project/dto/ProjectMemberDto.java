package org.crops.fitserver.domain.project.dto;

import lombok.Builder;
import org.crops.fitserver.domain.project.entity.ProjectMember;
import org.crops.fitserver.domain.skillset.constant.PositionType;

@Builder
public record ProjectMemberDto(
    Long userId,
    String username,
    String profileImageUrl,
    Long positionId
) {

  public static ProjectMemberDto from(ProjectMember projectMember) {
    return ProjectMemberDto.builder()
        .userId(projectMember.getUser().getId())
        .username(projectMember.getUser().getUsername())
        .profileImageUrl(projectMember.getUser().getProfileImageUrl())
        .positionId(projectMember.getPosition().getId())
        .build();
  }
}
