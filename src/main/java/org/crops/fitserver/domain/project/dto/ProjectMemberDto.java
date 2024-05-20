package org.crops.fitserver.domain.project.dto;

import lombok.Builder;
import org.crops.fitserver.domain.project.domain.ProjectMember;

@Builder
public record ProjectMemberDto(
    Long userId,
    String nickname,
    String profileImageUrl,
    Long positionId
) {

  public static ProjectMemberDto from(ProjectMember projectMember) {
    return ProjectMemberDto.builder()
        .userId(projectMember.getUser().getId())
        .nickname(projectMember.getUser().getNickname())
        .profileImageUrl(projectMember.getUser().getProfileImageUrl())
        .positionId(projectMember.getPosition().getId())
        .build();
  }
}
