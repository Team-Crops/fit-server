package org.crops.fitserver.domain.user.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.repository.UserPolicyAgreementRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
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
    var position = updateUserRequest.getPositionId() != null ?
        positionRepository.findById(updateUserRequest.getPositionId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION))
        : null;
    var region = updateUserRequest.getRegionId() != null ?
        regionRepository.findById(updateUserRequest.getRegionId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION))
        : null;
    var skillList = CollectionUtils.isEmpty(updateUserRequest.getSkillIdList()) ?
        skillRepository.findAllById(updateUserRequest.getSkillIdList())
        : new ArrayList<Skill>();

    user = user
        .withProfileImageUrl(updateUserRequest.getProfileImageUrl())
        .withUsername(updateUserRequest.getUsername())
        .withNickname(updateUserRequest.getNickname())
        .withPhoneNumber(updateUserRequest.getPhoneNumber())
        .withIsOpenPhoneNum(updateUserRequest.getIsOpenPhoneNum())
        .withEmail(updateUserRequest.getEmail());

    user.getUserInfo()
        .withBackground(updateUserRequest.getBackgroundStatus(),
            updateUserRequest.getBackgroundText())
        .withPortfolioUrl(updateUserRequest.getPortfolioUrl())
        .withProjectCount(updateUserRequest.getProjectCount())
        .withActivityHour(updateUserRequest.getActivityHour())
        .withIntroduce(updateUserRequest.getIntroduce())
        .withLinkJson(Link.parseToJson(updateUserRequest.getLinkList()))
        .withIsOpenProfile(updateUserRequest.getIsOpenProfile())
        .withPosition(position)
        .withRegion(region)
        .withSkills(skillList);
    return user;
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
