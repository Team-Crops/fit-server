package org.crops.fitserver.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(
    @NotNull String content
) {
}
