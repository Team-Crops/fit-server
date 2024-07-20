package org.crops.fitserver.domain.skillset.dto.response;

import java.util.List;
import org.crops.fitserver.domain.skillset.dto.SkillDto;

public record GetSkillListResponse(List<SkillDto> skillList) {

  public static GetSkillListResponse of(List<SkillDto> skillList) {
    return new GetSkillListResponse(skillList);
  }

}
