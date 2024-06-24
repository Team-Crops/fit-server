package org.crops.fitserver.global.socket.service;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;

@Slf4j
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
  protected OffsetDateTime createdAt;

  public static MessageResponse from(Message message) {
    switch (message.getMessageType()) {
      case TEXT:
        return TextMessageResponse.from(message);
      case IMAGE:
        return ImageMessageResponse.from(message);
      case NOTICE, JOIN, EXIT, COMPLETE:
        return NoticeMessageResponse.from(message);
      default:
        log.error("Unknown message type: {}", message.getMessageType());
        throw new IllegalArgumentException("Unknown message type");
    }
  }
}