package org.crops.fitserver.global.socket.service;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonTypeInfo(use = DEDUCTION)
@JsonSubTypes({
    @Type(value = NoticeMessageResponse.class),
    @Type(value = TextMessageResponse.class),
    @Type(value = ImageMessageResponse.class)
})
public abstract class MessageResponse implements org.crops.fitserver.global.mq.dto.Message {

  protected Long messageId;
  protected MessageType messageType;
  protected LocalDateTime createdAt;

  public static MessageResponse from(Message message) {
    if (message.getMessageType() == MessageType.TEXT) {
      return TextMessageResponse.from(message);
    } else if (message.getMessageType() == MessageType.IMAGE) {
      return ImageMessageResponse.from(message);
    } else if (message.getMessageType() == MessageType.NOTICE) {
      return NoticeMessageResponse.from(message);
    } else {
      throw new IllegalArgumentException("Unknown message type");
    }
  }
}