package org.crops.fitserver.domain.skillset.dto;

import lombok.Builder;
import org.crops.fitserver.domain.skillset.domain.Skill;

@Builder
public record SkillDto(Long id, String displayName) {

  public static SkillDto from(Skill save) {
    return SkillDto.builder()
        .id(save.getId())
        .displayName(save.getDisplayName())
        .build();
  }
}
