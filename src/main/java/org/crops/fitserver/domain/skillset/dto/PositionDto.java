package org.crops.fitserver.domain.skillset.dto;


import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.skillset.domain.Position;

@Builder
public record PositionDto(Long id, String displayName, String displayNameEn, List<SkillDto> skillList) {

  public static PositionDto from(Position position) {
    return PositionDto.builder()
        .id(position.getId())
        .displayName(position.getDisplayName())
        .displayNameEn(position.getDisplayNameEn())
        .skillList(position.getSkillSets().stream().map(
            skillSet -> SkillDto.from(skillSet.getSkill())
        ).toList())
        .build();
  }
}
