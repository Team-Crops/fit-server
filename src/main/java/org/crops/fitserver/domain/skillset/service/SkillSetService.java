package org.crops.fitserver.domain.skillset.service;

import java.util.List;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.CreatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;
import org.crops.fitserver.domain.skillset.dto.request.UpdatePositionRequest;

public interface SkillSetService {

  SkillDto createSkill(CreateSkillRequest createSkillRequest);

  PositionDto createPosition(CreatePositionRequest request);

  List<PositionDto> getPositionList();

  List<SkillDto> getSkillList();


  List<SkillDto> getSkillListByPositionId(Long positionId);

  SkillDto updateSkillDisplayName(Long skillId, String displayName);

  SkillDto addSkillToPositionList(Long SkillId, List<Long> positionIds);

  PositionDto updatePosition(Long positionId, UpdatePositionRequest request);

  PositionDto addSkillListToPosition(Long positionId, List<Long> skillIds);

  void deleteSkill(Long skillId);

  void deletePosition(Long positionId);
}
