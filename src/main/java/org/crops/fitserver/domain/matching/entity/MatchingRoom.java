package org.crops.fitserver.domain.matching.entity;

import static java.util.stream.Collectors.groupingBy;
import static org.crops.fitserver.domain.matching.constant.MatchingConstants.MAXIMUM_POSITIONS;
import static org.crops.fitserver.domain.matching.constant.MatchingConstants.MINIMUM_REQUIRED_POSITIONS;
import static org.crops.fitserver.domain.matching.constant.MatchingConstants.MULTIPLE_POSITION_COMPARE_TO_OTHER_POSITIONS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
@ToString
public class MatchingRoom extends BaseTimeEntity {

  @Id
  @Column(name = "matching_room_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "chat_room_id", nullable = false)
  private Long chatRoomId;
  @Column(name = "is_completed", nullable = false)
  private Boolean isCompleted;
  @Column(name = "completed_at", nullable = true)
  private LocalDateTime completedAt;
  @Column(name = "host_user_id", nullable = false)
  private Long hostUserId;
  @OneToMany(mappedBy = "matchingRoom", fetch = FetchType.EAGER)
  private final List<Matching> matchingList = new ArrayList<>();

  public static MatchingRoom create(List<Matching> matchingList, Long chatRoomId) {
    if (!canCreateRoom(matchingList)) {
      throw new BusinessException(ErrorCode.NOT_ENOUGH_MATCHING_EXCEPTION);
    }
    var newMatchingRoom = MatchingRoom.builder()
        .chatRoomId(chatRoomId)
        .isCompleted(false)
        .completedAt(null)
        .hostUserId(selectHostUserId(matchingList))
        .build();
    matchingList.forEach(newMatchingRoom::addMatching);

    return newMatchingRoom;
  }

  private static boolean canCreateRoom(List<Matching> matchingList) {
    return matchingList.stream().map(Matching::getPosition).distinct().count()
        >= MINIMUM_REQUIRED_POSITIONS.size();
  }

  private static Long selectHostUserId(List<Matching> matchingList) {
    return matchingList.stream()
        .filter(m -> PositionType.PLANNER.equals(m.getPosition().getType()))
        .findFirst()
        .orElse(matchingList.get(0))
        .getUser().getId();
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

    matching.forceOut();
    matchingList.remove(matching);
  }

  public void exit(Matching matching) {
    if (matchingList.stream().noneMatch(m -> m.getId().equals(matching.getId()))) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION);
    }
    if (matching.isHost()) {
      changeHost();
    }
    matching.exit();
    matchingList.remove(matching);
  }

  public void changeHost() {
    var newHost = matchingList.stream()
        .filter(m -> !m.getUser().getId().equals(hostUserId))
        .findAny()
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    hostUserId = newHost.getUser().getId();
  }

  public boolean isNotEnough() {
    return !this.isEnough();
  }

  public boolean isEnough() {
    return MINIMUM_REQUIRED_POSITIONS.keySet().stream()
        .allMatch(this::isEnough);
  }

  public boolean isNotEnough(PositionType positionType) {
    return this.isEnough(positionType);
  }

  public boolean isEnough(PositionType positionType) {
    return matchingList.stream()
        .filter(m -> m.getPosition().getType().equals(positionType))
        .count() >= MINIMUM_REQUIRED_POSITIONS.get(positionType);
  }

  public boolean canJoinRoom(Matching matching) {
    var positionType = matching.getPosition().getType();
    if (!canInsertPosition(positionType)) {
      return false;
    }

    var requiredPositionId = getRequiredPositionId(positionType);
    if (requiredPositionId.isPresent() && !requiredPositionId.get()
        .equals(matching.getPosition().getId())) {
      return false;
    }

    var requiredSkillIds = getRequiredSkillIds(positionType);
    if (requiredSkillIds.isEmpty()) {
      return true;
    }
    var userSkillIds = matching.getUser().getUserInfo().getUserInfoSkills().stream()
        .map(userInfoSkill -> userInfoSkill.getSkill().getId())
        .toList();
    var skillIdsIntersection = new HashSet<>(requiredSkillIds);
    skillIdsIntersection.retainAll(userSkillIds);

    return !skillIdsIntersection.isEmpty();
  }

  /**
   * 현재 포지션이 다른 포지션의 최대 인원수보다 2배이상 많으면 안된다. MAXIMUM_POSITIONS에 제한된 인원수를 넘으면 안된다.
   */
  public boolean canInsertPosition(PositionType positionType) {

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

  /**
   * 백엔드와 프론트엔드는 포지션 id까지 일치해야 한다.
   */
  public Optional<Long> getRequiredPositionId(PositionType positionType) {
    if (PositionType.PLANNER.equals(positionType) || PositionType.DESIGNER.equals(positionType)) {
      return Optional.empty();
    }
    return matchingList.stream()
        .filter(m -> m.getPosition().getType().equals(positionType))
        .map(m -> m.getPosition().getId())
        .findFirst();
  }

  /**
   * 플래너와 디자이너는 필요한 스킬이 없다. 백엔드와 프론트엔드는 기존에 있는 사람과 skill이 일치하는 것이 존재해야 한다.
   */
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
            .anyMatch(skillSet -> skillSet.isEqualPositionType(positionType)))
        .map(Skill::getId)
        .toList();
  }

  public void addMatching(Matching matching) {
    matchingList.add(matching);
    matching.match(this);
  }

  public void ready(Matching matching) {
    if (matchingList.stream().noneMatch(m -> m.getId().equals(matching.getId()))) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION);
    }
    matching.ready();
  }

  public void cancelReady(Matching matching) {
    if (matchingList.stream().noneMatch(m -> m.getId().equals(matching.getId()))) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION);
    }
    matching.cancelReady();
  }

  public void complete() {

    if (matchingList.stream().filter((matching -> !matching.isHost())).anyMatch(m -> !m.isReady())) {
      throw new BusinessException(ErrorCode.NOT_READY_MATCHING_EXCEPTION);
    }
    isCompleted = true;
    completedAt = LocalDateTime.now();
    matchingList.forEach(Matching::complete);
  }
}
