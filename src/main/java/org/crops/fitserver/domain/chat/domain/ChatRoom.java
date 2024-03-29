package org.crops.fitserver.domain.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE chat_room SET is_deleted = true WHERE chat_room_id = ?")
public class ChatRoom extends BaseTimeEntity {

  @Id
  @Column(name = "chat_room_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public static ChatRoom newInstance() {
    return ChatRoom.builder()
        .build();
  }
}
