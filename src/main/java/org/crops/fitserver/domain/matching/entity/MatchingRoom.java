package org.crops.fitserver.domain.matching.entity;

import static java.util.stream.Collectors.groupingBy;
import static org.crops.fitserver.domain.matching.constant.Constant.MAXIMUM_POSITIONS;
import static org.crops.fitserver.domain.matching.constant.Constant.MINIMUM_REQUIRED_POSITIONS;
import static org.crops.fitserver.domain.matching.constant.Constant.MULTIPLE_POSITION_COMPARE_TO_OTHER_POSITIONS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.domain.UserInfoSkill;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
public class MatchingRoom extends BaseTimeEntity {

  @Id
  @Column(name = "matching_room_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "chat_room_id", nullable = false)
  private Long chatRoomId;

  @Column(name = "is_complete", nullable = false)
  private Boolean isComplete;

  @Column(name = "completed_at", nullable = true)
  private LocalDateTime completedAt;

  @Column(name = "host_user_id", nullable = false)
  private Long hostUserId;

  @OneToMany(mappedBy = "matchingRoom")
  private List<Matching> matchingList = new ArrayList<>();

  public static MatchingRoom createRoom(List<Matching> matchingList, Long chatRoomId) {
    if (matchingList.stream().map(Matching::getPosition).distinct().count() < 4) {
      throw new BusinessException(ErrorCode.NOT_ENOUGH_MATCHING_EXCEPTION);
    }
    return MatchingRoom.builder()
        .matchingList(matchingList)
        .chatRoomId(chatRoomId)
        .isComplete(false)
        .completedAt(null)
        .hostUserId(matchingList.stream()
            .filter(m -> PositionType.PLANNER.equals(m.getPosition().getType())).findFirst()
            .orElse(matchingList.get(0))
            .getUser().getId())
        .build();
  }

  public void forceOut(Long userId, Long forceOutUserId) {
    if (!hostUserId.equals(userId)) {
      throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION);
    }
    if (forceOutUserId.equals(userId)) {
      throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION);
    }

    var matching = matchingList.stream()
        .filter(m -> m.getUser().getId().equals(forceOutUserId))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    matching.cancel();
  }

  public void changeHost() {
    //현재 호스트를 제외하고 완전 랜덤하게 호스트 변경
    var newHost = matchingList.stream()
        .filter(m -> !m.getUser().getId().equals(hostUserId))
        .findAny()
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    hostUserId = newHost.getUser().getId();
  }

  public boolean isNotEnough() {
    return MINIMUM_REQUIRED_POSITIONS.keySet().stream()
        .anyMatch(positionType -> matchingList.stream()
            .noneMatch(m -> m.getPosition().getType().equals(positionType)));
  }

  public boolean isNotEnough(PositionType positionType) {
    return matchingList.stream()
        .filter(m -> m.getPosition().getType().equals(positionType))
        .count() < MINIMUM_REQUIRED_POSITIONS.get(positionType);
  }

  //현재 포지션이 다른 포지션의 최대 인원수보다 2배이상 많으면 안된다.
  //MAXIMUM_POSITIONS에 제한된 인원수를 넘으면 안된다.
  public boolean isCanInsertPosition(PositionType positionType) {

    var otherPositionMinSize = matchingList.stream()
        .filter(m -> !m.getPosition().getType().equals(positionType))
        .collect(groupingBy(matching -> matching.getPosition().getType()))
        .values().stream().map(List::size).min(Integer::compareTo).orElse(0);

    var limitSize = Math.min(
        otherPositionMinSize * MULTIPLE_POSITION_COMPARE_TO_OTHER_POSITIONS.get(positionType),
        MAXIMUM_POSITIONS.get(positionType)
    );

    var nowSize = matchingList.stream()
        .filter(m -> m.getPosition().getType().equals(positionType))
        .count();

    return nowSize < limitSize;
  }

  //플래너와 디자이너는 필요한 스킬이 없다.
  //백엔드와 프론트엔드는 기존에 있는 사람과 skill이 일치하는 것이 존재해야 한다.
  public List<Long> getRequiredSkillIds(PositionType positionType) {
    if (PositionType.PLANNER.equals(positionType) || PositionType.DESIGNER.equals(positionType)) {
      return List.of();
    }
    return matchingList.stream()
        .filter(m -> m.getPosition().getType().equals(positionType))
        .map(m -> m.getUser().getUserInfo().getUserInfoSkills())
        .flatMap(List::stream)
        .map(UserInfoSkill::getSkill)
        .distinct()
        .filter(skill -> skill.getSkillSets().stream()
            .anyMatch(skillSet -> skillSet.getPosition().getType().equals(positionType)))
        .map(Skill::getId)
        .toList();
  }

  public void addMatching(Matching matching) {
    matchingList.add(matching);
    matching.match(this);
  }
}