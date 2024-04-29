package org.crops.fitserver.domain.chat.domain;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends BaseTimeEntity {

  @Id
  @Column(name = "message_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id", nullable = false)
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 20)
  @ColumnDefault(value = "'TEXT'")
  private MessageType messageType;

  private String content;

  public static Message newInstance(
      User user,
      ChatRoom chatRoom,
      MessageType messageType,
      String content) {
    return Message.builder()
        .user(user)
        .chatRoom(chatRoom)
        .messageType(messageType)
        .content(content)
        .build();
  }

  public static Message newInstance(
      ChatRoom chatRoom,
      MessageType messageType,
      String content) {
    return Message.builder()
        .chatRoom(chatRoom)
        .messageType(messageType)
        .content(content)
        .build();
  }
}
