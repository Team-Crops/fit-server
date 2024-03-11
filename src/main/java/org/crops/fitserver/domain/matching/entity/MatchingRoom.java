package org.crops.fitserver.domain.matching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

  @Column(name = "is_matched", nullable = false)
  private Boolean isMatched;

  @Column(name = "matched_at", nullable = true)
  private Long matchedAt;

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
        .isMatched(false)
        .matchedAt(null)
        .hostUserId(matchingList.get(0).getUser().getId())
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
}
