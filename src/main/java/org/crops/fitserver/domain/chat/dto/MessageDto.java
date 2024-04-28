package org.crops.fitserver.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;

@Builder
public record MessageDto(
    Long userId,
    Long messageId,
    String content,
    LocalDateTime createdAt,
    MessageType messageType
) {

  public static MessageDto from(Message message) {
        return MessageDto.builder()
            .userId(message.getUser().getId())
            .content(message.getContent())
            .createdAt(message.getCreatedAt())
            .messageId(message.getId())
            .messageType(message.getMessageType())
            .build();
    }
}
