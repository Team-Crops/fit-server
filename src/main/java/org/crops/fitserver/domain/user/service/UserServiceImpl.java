package org.crops.fitserver.domain.user.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserInfoSkill;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.repository.UserPolicyAgreementRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PositionRepository positionRepository;
  private final RegionRepository regionRepository;
  private final SkillRepository skillRepository;
  private final UserPolicyAgreementRepository userPolicyAgreementRepository;

  @Override
  public User getUserWithInfo(Long userId) {
    return userRepository.findWithInfo(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  @Transactional
  public User updateUserWithInfo(Long userId, UpdateUserRequest updateUserRequest) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));

    user = user
        .withProfileImageUrl(
            updateUserRequest.getProfileImageUrl().orElse(user.getProfileImageUrl()))
        .withUsername(updateUserRequest.getUsername().orElse(user.getUsername()))
        .withNickname(updateUserRequest.getNickname().orElse(user.getNickname()))
        .withPhoneNumber(updateUserRequest.getPhoneNumber().orElse(user.getPhoneNumber()))
        .withIsOpenPhoneNum(updateUserRequest.getIsOpenPhoneNum().orElse(user.isOpenPhoneNum()))
        .withEmail(updateUserRequest.getEmail().orElse(user.getEmail()));

    user.getUserInfo()
        .withBackground(updateUserRequest.getBackgroundStatus(),
            updateUserRequest.getBackgroundText())
        .withPortfolioUrl(
            updateUserRequest.getPortfolioUrl().orElse(user.getUserInfo().getPortfolioUrl()))
        .withProjectCount(
            updateUserRequest.getProjectCount().orElse(user.getUserInfo().getProjectCount()))
        .withActivityHour(
            updateUserRequest.getActivityHour().orElse(user.getUserInfo().getActivityHour()))
        .withIntroduce(updateUserRequest.getIntroduce().orElse(user.getUserInfo().getIntroduce()))
        .withLinkJson(updateUserRequest.getLinkList().isPresent() ? Link.parseToJson(
            updateUserRequest.getLinkList().orElse(List.of())) : user.getUserInfo().getLinkJson())
        .withIsOpenProfile(
            updateUserRequest.getIsOpenProfile().orElse(user.getUserInfo().isOpenProfile()))
        .withPosition(getNewPosition(updateUserRequest.getPositionId(), user))
        .withRegion(getNewRegion(updateUserRequest.getRegionId(), user))
        .withSkills(getNewSkillList(updateUserRequest.getSkillIdList(), user));

    user = userRepository.save(user);
    return user;
  }

  private Position getNewPosition(JsonNullable<Long> positionId, User user) {
    if (!positionId.isPresent()) {
      return user.getUserInfo().getPosition();
    }
    if (positionId.get() == null) {
      return null;
    }
    return positionRepository.findById(positionId.get()).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  private Region getNewRegion(JsonNullable<Long> regionId, User user) {
    if (!regionId.isPresent()) {
      return user.getUserInfo().getRegion();
    }
    if (regionId.get() == null) {
      return null;
    }
    return regionRepository.findById(regionId.get()).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  private List<Skill> getNewSkillList(JsonNullable<List<Long>> skillIdList, User user) {
    if (!skillIdList.isPresent()) {
      return user.getUserInfo().getUserInfoSkills().stream().map(UserInfoSkill::getSkill).toList();
    }
    if (CollectionUtils.isEmpty(skillIdList.get())) {
      return new ArrayList<>();
    }
    return skillRepository.findAllById(skillIdList.get());
  }

  public List<UserPolicyAgreement> getPolicyAgreementList(Long userId) {
    return userPolicyAgreementRepository.findAllByUserId(userId).stream()
        .toList();
  }

  @Override
  @Transactional
  public List<UserPolicyAgreement> updatePolicyAgreement(Long userId,
      List<PolicyAgreementDto> policyAgreementDtoList) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var existPolicyAgreementList = userPolicyAgreementRepository.findAllByUserId(userId);

    var policyAgreementListForSave = policyAgreementDtoList.stream().map(
        dto -> existPolicyAgreementList.stream()
            .filter(
                policyAgreement -> policyAgreement.getPolicyType().equals(dto.policyType()))
            .findFirst()
            .map(policyAgreement -> policyAgreement.updateIsAgree(dto.isAgree()))
            .orElse(UserPolicyAgreement.builder()
                .user(user)
                .policyType(dto.policyType())
                .version(dto.version())
                .isAgree(dto.isAgree())
                .build())
    ).toList();

    return userPolicyAgreementRepository.saveAll(policyAgreementListForSave);
  }
}
