package org.crops.fitserver.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PositionRepository positionRepository;
  private final RegionRepository regionRepository;
  private final SkillRepository skillRepository;

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
    var position = positionRepository.findById(updateUserRequest.getPositionId())
        .orElseThrow(() -> new BusinessException(
            ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var region = regionRepository.findById(updateUserRequest.getRegionId())
        .orElseThrow(() -> new BusinessException(
            ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var skillList = skillRepository.findAllById(updateUserRequest.getSkillIdList());

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
}
