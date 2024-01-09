package org.crops.fitserver.domain.chat.controller.dto.response;

import jakarta.validation.constraints.NotNull;

public record StringMessageResponse(
    @NotNull String content
) {

}