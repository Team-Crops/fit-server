package org.crops.fitserver.domain.matching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
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
  private List<Matching> matchings;
}
