package org.crops.fitserver.domain.chat.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.annotation.ChatMessageType;

public record SendMessageRequest(
    @NotNull String content,
    @ChatMessageType MessageType messageType
) {
}
