package org.crops.fitserver.domain.chat.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import org.crops.fitserver.domain.chat.domain.MessageType;

public record SendMessageRequest(
    @NotNull String content,
    @NotNull MessageType messageType
) {
}
