package org.crops.fitserver.global.socket.service;

import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;

@Getter
public abstract class MessageResponse {

  protected Long messageId;
  protected MessageType messageType;

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