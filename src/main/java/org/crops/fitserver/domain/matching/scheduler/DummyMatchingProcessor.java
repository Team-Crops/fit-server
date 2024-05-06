package org.crops.fitserver.domain.matching.scheduler;

import static org.crops.fitserver.global.util.JsonUtil.toJson;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.domain.user.constant.LinkType;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.User.UserBuilder;
import org.crops.fitserver.domain.user.domain.UserInfo;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DummyMatchingProcessor {
  private final PositionRepository positionRepository;
  private final SkillRepository skillRepository;
  private final RegionRepository regionRepository;
  private final UserRepository userRepository;

  private static final int DUMMY_USER_COUNT = 10;

  public void validateSkillSet() {
    var positionList = positionRepository.findAll();
    if(positionList.isEmpty()) {
      throw new IllegalArgumentException("Position is empty");
    }
    var skillList = skillRepository.findAll();
    if(skillList.isEmpty()) {
      throw new IllegalArgumentException("Skill is empty");
    }
  }

  public List<User> prepareUser() {
    var newUserList = new ArrayList<User>();
    var positionList = positionRepository.findAll();
    var regionList = regionRepository.findAll();

    for (int i = 0; i < DUMMY_USER_COUNT; i++) {
      var username = "test-" + RandomStringUtils.randomAlphanumeric(10);
      var nickname = "test-" + RandomStringUtils.randomAlphanumeric(10);
      var email = "test" + i + "@gmail.com";
      var region = regionList.get(i % regionList.size());
      var position = positionList.get(i % positionList.size());
      var skill = position.getSkills().stream()
          .min((o1, o2) -> (int) (Math.random() * 10) - 1).get();

      var newUser = createUser(username, nickname, email, region, position, skill);
      newUserList.add(newUser);
    }

    userRepository.saveAllAndFlush(newUserList);

    log.info("newUserList size : {}", newUserList.size());

    return newUserList;
  }

  private User createUser(String username, String nickname, String email, Region region, Position position, Skill skill) {
    var user = buildNewUser()
        .username(username)
        .nickname(nickname)
        .email(email)
        .build();
    user.getUserInfo()
        .withProjectCount((int) (Math.random()* 5))
        .withActivityHour((short) (Math.random() * 24))
        .withRegion(region)
        .withPosition(position)
        .withSkills(List.of(skill));
    return user;
  }
  public static UserBuilder buildNewUser() {
    var linkList = List.of(
        Link.builder().linkType(LinkType.GITHUB).linkUrl("github.com").build(),
        Link.builder().linkType(LinkType.ETC).linkUrl("test.com").build()
    );
    var userInfo = UserInfo.builder()
        .portfolioUrl("test.com")
        .introduce("test")
        .isOpenProfile(true)
        .linkJson(toJson(linkList))
        .status(UserInfoStatus.COMPLETE)
        .build();
    return User.builder()
        .userRole(UserRole.MEMBER)
        .profileImageUrl("test.com")
        .phoneNumber("010-1234-1234")
        .isOpenPhoneNum(true)
        .email("test@gmail.com")
        .userInfo(userInfo)
        ;
  }
}
