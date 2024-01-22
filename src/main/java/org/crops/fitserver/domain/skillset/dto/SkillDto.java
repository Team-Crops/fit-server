package org.crops.fitserver.domain.skillset.dto;

import lombok.AccessLevel;
import lombok.Builder;
import org.crops.fitserver.domain.skillset.domain.Skill;

@Builder
public record SkillDto(Long id, String displayName) {

  public static SkillDto from(Skill skill) {
    return SkillDto.builder()
        .id(skill.getId())
        .displayName(skill.getDisplayName())
        .build();
  }
}
