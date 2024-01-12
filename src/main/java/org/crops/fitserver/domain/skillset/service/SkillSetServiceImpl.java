package org.crops.fitserver.domain.skillset.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
class SkillSetServiceImpl implements SkillSetService {

  private final SkillRepository skillRepository;
  private final PositionRepository positionRepository;

  @Override
  @Transactional
  public SkillDto createSkill(CreateSkillRequest createSkillRequest) {
    if (skillRepository.findByDisplayName(createSkillRequest.displayName()).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }

    //1. Skill 생성
    var skill = skillRepository.save(
        Skill.builder().displayName(createSkillRequest.displayName()
        ).build()
    );

    //2. Position에 Skill 추가
    if (!CollectionUtils.isEmpty(createSkillRequest.positionIds())) {
      var positions = positionRepository.findAllById(createSkillRequest.positionIds());

      if (positions.size() != createSkillRequest.positionIds().size()) {
        throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
      }

      positions.forEach(position -> position.addSkill(skill));

      positionRepository.saveAll(positions);
    }

    return SkillDto.from(skill);
  }
}
