package org.crops.fitserver.domain.chat.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.dto.response.GetMessageListResponse;
import org.crops.fitserver.domain.chat.facade.MessageFacade;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

  private final MessageFacade messageFacade;

  @GetMapping("/room/{roomId}")
  public ResponseEntity<GetMessageListResponse> getMessages(
      @CurrentUserId Long userId,
      @NotNull @PathVariable long roomId,
      @NotNull @RequestParam int page
  ) {
    var response = GetMessageListResponse.of(
        messageFacade.getMessages(userId, roomId, page));
    return ResponseEntity.ok(response);
  }
}
