package org.crops.fitserver.domain.skillset.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.file.service.FileService;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.CreatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;
import org.crops.fitserver.domain.skillset.dto.request.UpdatePositionRequest;
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
  private final FileService fileService;

  @Override
  @Transactional
  public SkillDto createSkill(CreateSkillRequest createSkillRequest) {
    if (skillRepository.existsByDisplayName(createSkillRequest.displayName())) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }

    //1. Skill 생성
    var skill = Skill.builder().displayName(createSkillRequest.displayName()).build();
    skill = skillRepository.save(skill);

    //2. Position에 Skill 추가
    if (!CollectionUtils.isEmpty(createSkillRequest.positionIds())) {
      addSkillToPositionList(skill, createSkillRequest.positionIds());
    }

    return SkillDto.from(skill);
  }

  @Override
  @Transactional
  public PositionDto createPosition(CreatePositionRequest request) {
    handleDuplicatedDisplayName(request.displayName());
    validateImageUrl(request.imageUrl());

    var position = Position.builder().displayName(request.displayName())
        .imageUrl(request.imageUrl()).build();
    position = positionRepository.save(position);

    if (!CollectionUtils.isEmpty(request.skillIds())) {
      addSkillListToPosition(position, request.skillIds());
    }

    return PositionDto.from(position);
  }

  @Override
  public List<PositionDto> getPositionList() {
    return positionRepository.findAll().stream().map(PositionDto::from).toList();
  }

  @Override
  public List<SkillDto> getSkillList() {
    return skillRepository.findAll().stream().map(SkillDto::from).toList();
  }

  @Override
  public List<SkillDto> getSkillListByPositionId(Long positionId) {
    return positionRepository.findWithSkills(positionId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION))
        .getSkillSets()
        .stream()
        .map(skillSet -> SkillDto.from(skillSet.getSkill()))
        .toList();
  }

  @Override
  @Transactional
  public SkillDto updateSkillDisplayName(Long skillId, String displayName) {
    return skillRepository.findById(skillId)
        .map(skill -> {
          skill.updateDisplayName(displayName);
          return SkillDto.from(skill);
        })
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  @Transactional
  public SkillDto addSkillToPositionList(Long SkillId, List<Long> positionIds) {
    return skillRepository.findById(SkillId)
        .map(skill -> {
          addSkillToPositionList(skill, positionIds);
          return SkillDto.from(skill);
        })
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  @Transactional
  public PositionDto updatePosition(Long positionId, UpdatePositionRequest request) {
    if(request.displayName().isPresent()) {
      handleDuplicatedDisplayName(request.displayName().get());
    }
    if(request.displayNameEn().isPresent()) {
      handleDuplicatedDisplayNameEn(request.displayNameEn().get());
    }
    if(request.imageUrl().isPresent()){
      validateImageUrl(request.imageUrl().get());
    }

    return positionRepository.findById(positionId)
        .map(position -> {
          position.updateDisplayName(request.displayName().orElse(position.getDisplayName()));
          position.updateDisplayNameEn(request.displayNameEn().orElse(position.getDisplayNameEn()));
          position.updateImageUrl(request.imageUrl().orElse(position.getImageUrl()));
          return PositionDto.from(position);
        })
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  @Transactional
  public PositionDto addSkillListToPosition(Long positionId, List<Long> skillIds) {
    return positionRepository.findById(positionId)
        .map(position -> {
          addSkillListToPosition(position, skillIds);
          return PositionDto.from(position);
        })
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  @Transactional
  public void deleteSkill(Long skillId) {
    skillRepository.deleteById(skillId);
  }

  @Override
  @Transactional
  public void deletePosition(Long positionId) {
    positionRepository.deleteById(positionId);
  }

  private void handleDuplicatedDisplayName(String displayName) {
    if (positionRepository.findByDisplayName(displayName).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }
  }

  private void handleDuplicatedDisplayNameEn(String displayNameEn) {
    if (positionRepository.findByDisplayName(displayNameEn).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }
  }

  private void validateImageUrl(String imageUrl) {
    if (!fileService.isUploaded(imageUrl)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }
  }

  /**
   * private이기 때문에 부모의 트랜잭션을 이어받음.
   */
  private void addSkillToPositionList(Skill skill, List<Long> positionIds) {
    var positions = positionRepository.findAllById(positionIds);

    if (positions.size() != positionIds.size()) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }

    positions.forEach(position -> position.addSkill(skill));

    positionRepository.saveAll(positions);
  }

  private void addSkillListToPosition(Position position, List<Long> skillIds) {
    var skills = skillRepository.findAllById(skillIds);

    if (skills.size() != skillIds.size()) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }

    skills.forEach(position::addSkill);

    positionRepository.save(position);
  }
}
