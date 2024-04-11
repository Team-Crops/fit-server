package org.crops.fitserver.domain.matching.entity;

import static org.crops.fitserver.domain.matching.constant.MatchingConstants.MATCHING_EXPIRE_DAYS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.user.domain.User;
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
public class Matching extends BaseTimeEntity {

  @Id
  @Column(name = "matching_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "matching_room_id", nullable = true)
  private MatchingRoom matchingRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "position_id", nullable = false)
  private Position position;

  @Column(name = "expired_at", nullable = true)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDateTime expiredAt;

  @Column(name = "last_batch_at", nullable = true)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDateTime lastBatchAt;

  @Column(name = "status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private MatchingStatus status;

  public static Matching create(User user) {
    return Matching.builder()
        .user(user)
        .position(user.getUserInfo().getPosition())
        .status(MatchingStatus.WAITING)
        .expiredAt(LocalDateTime.now().plusDays(MATCHING_EXPIRE_DAYS))
        .build();
  }

  public void expire() {
    this.status = MatchingStatus.EXPIRED;
    //매칭룸에서 자신이 호스트라면 매칭룸에서 다른 유저에게 호스트를 넘긴다.
    if (isHost()) {
      this.matchingRoom.changeHost();
    }
    this.matchingRoom = null;
  }

  public void updateLastBatchAt() {
    this.lastBatchAt = LocalDateTime.now();
  }

  public boolean isHost() {
    return matchingRoom != null && matchingRoom.getHostUserId().equals(user.getId());
  }


  protected void match(MatchingRoom matchingRoom) {
    this.status = MatchingStatus.MATCHED;
    this.matchingRoom = matchingRoom;
    this.expiredAt = null;
  }

  public void cancel() {
    if(this.matchingRoom != null){
      throw new BusinessException(ErrorCode.ALREADY_EXIST_MATCHING_ROOM_EXCEPTION);
    }
    this.status = MatchingStatus.CANCELED;
  }
  protected void forceOut() {
    this.status = MatchingStatus.FORCED_OUT;
    this.matchingRoom = null;
  }
  protected void exit() {
    this.status = MatchingStatus.EXITED;
    this.matchingRoom = null;
  }

  protected void complete() {
    this.status = MatchingStatus.COMPLETED;
  }

  protected void ready() {
    if (this.status != MatchingStatus.MATCHED) {
      throw new BusinessException(ErrorCode.NOT_ENABLE_READY_EXCEPTION);
    }
    if (isHost()) {
      throw new BusinessException(ErrorCode.NOT_ENABLE_READY_EXCEPTION);
    }
    this.status = MatchingStatus.ACCEPTED;
  }

  public boolean isReady() {
    return this.status == MatchingStatus.ACCEPTED;
  }
}
