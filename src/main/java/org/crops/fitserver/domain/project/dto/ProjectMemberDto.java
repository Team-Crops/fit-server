package org.crops.fitserver.domain.project.dto;

import lombok.Builder;
import org.crops.fitserver.domain.project.entity.ProjectMember;
import org.crops.fitserver.domain.skillset.constant.PositionType;

@Builder
public record ProjectMemberDto(
    Long projectMemberId,
    Long userId,
    String username,
    String profileImageUrl,
    PositionType positionType
) {

  public static ProjectMemberDto from(ProjectMember projectMember) {
    return ProjectMemberDto.builder()
        .projectMemberId(projectMember.getId())
        .userId(projectMember.getUser().getId())
        .username(projectMember.getUser().getUsername())
        .profileImageUrl(projectMember.getUser().getProfileImageUrl())
        .positionType(projectMember.getPosition().getType())
        .build();
  }
}
