package org.crops.fitserver.domain.matching.scheduler;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.config.UserBuildUtil;
import org.crops.fitserver.domain.matching.controller.MatchingController;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class MatchingProcessorTest {

  @Autowired
  EntityManager em;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MatchingRepository matchingRepository;
  @Autowired
  private MatchingRoomRepository matchingRoomRepository;
  @Autowired
  private MatchingProcessor sut;
  @Autowired
  private PositionRepository positionRepository;
  @Autowired
  private SkillRepository skillRepository;
  @Autowired
  private MatchingRoomRepository roomRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Autowired
  private MatchingController matchingController;

  private static final int USER_COUNT = 1000;


  @Test
  @Transactional
  void match() {
    prepareSkillSet();
    prepareRegion();
    prepareUser();

    var userList = userRepository.findAll();

    log.info("startTime : {}", System.currentTimeMillis());

    var matchingList = userList.stream().map(user -> matchingRepository.save(Matching.create(user)))
        .toList();

    log.info("startTime : {}", System.currentTimeMillis());


    sut.match();

    log.info("endTime : {}", System.currentTimeMillis());

    var newMatchingList = matchingRepository.findAll();

//    log.info("matchingList size : {}, matchingList : {}", matchingList.size(), matchingList);

    var roomList = roomRepository.findAll();

    log.info("roomList size : {}", roomList.size());

//    sut.match();

//    var matchingList2 = matchingRepository.findAll();
//
//    log.info("matchingList2 : {}", matchingList2);
//
//    var roomList2 = roomRepository.findAll();
//
//    log.info("roomList2 : {}", roomList2);


  }

  private void prepareSkillSet() {
    var newPositionList = List.of(
        Position.builder().displayName("백엔드").displayNameEn("backend").type(PositionType.BACKEND)
            .imageUrl("backendImage").build(),
        Position.builder().displayName("프론트엔드").displayNameEn("frontend")
            .type(PositionType.FRONTEND).imageUrl("frontendImage").build(),
        Position.builder().displayName("디자이너").displayNameEn("designer").type(PositionType.DESIGNER)
            .imageUrl("designerImage").build(),
        Position.builder().displayName("기획자").displayNameEn("planner").type(PositionType.PLANNER)
            .imageUrl("plannerImage").build()
    );

    positionRepository.saveAllAndFlush(newPositionList);

    var newSkillList = List.of(
        Skill.builder().displayName("Spring").build(),
        Skill.builder().displayName("Nestjs").build(),
        Skill.builder().displayName("React").build(),
        Skill.builder().displayName("Vue").build(),
        Skill.builder().displayName("Figma").build()
    );
    newSkillList.stream().filter(skill -> skill.getDisplayName().equals("Spring")).findFirst().get()
        .addPosition(newPositionList.get(0));
    newSkillList.stream().filter(skill -> skill.getDisplayName().equals("Nestjs")).findFirst().get()
        .addPosition(newPositionList.get(0));
    newSkillList.stream().filter(skill -> skill.getDisplayName().equals("React")).findFirst().get()
        .addPosition(newPositionList.get(1));
    newSkillList.stream().filter(skill -> skill.getDisplayName().equals("Vue")).findFirst().get()
        .addPosition(newPositionList.get(1));
    newSkillList.stream().filter(skill -> skill.getDisplayName().equals("Figma")).findFirst().get()
        .addPosition(newPositionList.get(2));
    newSkillList.stream().filter(skill -> skill.getDisplayName().equals("Figma")).findFirst().get()
        .addPosition(newPositionList.get(3));

    skillRepository.saveAllAndFlush(newSkillList);

    var positionList = positionRepository.findAll();

    log.info("positionList : {}", positionList);
  }

  private void prepareRegion() {

    var regionList = List.of(Region.builder().displayName("서울").build(),
        Region.builder().displayName("경기").build());

    regionRepository.saveAllAndFlush(regionList);
  }

  private void prepareUser() {
    var newUserList = new ArrayList<User>();
    var positionList = positionRepository.findAll();
    var skillList = skillRepository.findAll();
    var regionList = regionRepository.findAll();

    for (int i = 0; i < USER_COUNT; i++) {
      var username = "test" + i;
      var nickname = "test" + i;
      var email = "test" + i + "@gmail.com";
      var region = regionList.get(i % regionList.size());
      var position = positionList.get(i % positionList.size());
      var skill = skillList.stream().filter(s -> s.getPositions().contains(position))
          .min((o1, o2) -> (int) (Math.random() * 3) - 1).get();

      var newUser = createUser(username, nickname, email, region, position, skill);
      newUserList.add(newUser);
    }

    userRepository.saveAllAndFlush(newUserList);

    log.info("newUserList size : {}", newUserList.size());
  }

  private User createUser(String username, String nickname, String email, Region region, Position position, Skill skill) {
    var user = UserBuildUtil.buildNewUser()
        .username(username)
        .nickname(nickname)
        .email(email)
        .build();
    user.getUserInfo()
        .withRegion(region)
        .withPosition(position)
        .withSkills(List.of(skill));
    return user;
  }
}