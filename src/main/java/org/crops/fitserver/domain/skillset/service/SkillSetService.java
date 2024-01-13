package org.crops.fitserver.domain.skillset.service;

import java.util.List;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.CreatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;

public interface SkillSetService {

  SkillDto createSkill(CreateSkillRequest createSkillRequest);

  PositionDto createPosition(CreatePositionRequest request);

  List<PositionDto> getPostionList();

  List<SkillDto> getSkillList();


  List<SkillDto> getSkillListByPositionId(Long positionId);
}
