package org.crops.fitserver.domain.chat.controller.dto.response;

import jakarta.validation.constraints.NotNull;
import org.crops.fitserver.domain.chat.domain.MessageType;

public record ChatMessageResponse(
    @NotNull Long userId,
    @NotNull String userName,
    @NotNull String profileImageUrl,
    @NotNull MessageType messageType,
    @NotNull String content
) {

}