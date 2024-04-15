package org.crops.fitserver.domain.chat.dto;

import lombok.Builder;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;

@Builder
public record MessageDto(
    Long userId,
    String content,
    MessageType messageType
) {

  public static MessageDto from(Message message) {
        return MessageDto.builder()
            .userId(message.getUser().getId())
            .messageType(message.getMessageType())
            .content(message.getContent())
            .build();
    }
}
