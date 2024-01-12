package org.crops.fitserver.domain.skillset.service;

import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;

public interface SkillSetService {

  SkillDto createSkill(CreateSkillRequest createSkillRequest);
}
