package org.crops.fitserver.domain.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.dto.request.ReceiveMessageRequest;
import org.crops.fitserver.domain.chat.dto.request.SendMessageRequest;
import org.crops.fitserver.domain.chat.dto.response.GetMessageListResponse;
import org.crops.fitserver.domain.chat.dto.response.MessageIdResponse;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.SocketController;
import org.crops.fitserver.global.annotation.SocketMapping;
import org.crops.fitserver.global.annotation.V1;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequestMapping("/chat")
@SocketController("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatRoomFacade chatRoomFacade;
  private final SocketService socketService;

  @SocketMapping(endpoint = "/text", requestCls = SendMessageRequest.class)
  public void sendTextMessage(SocketIOClient client, @Valid SendMessageRequest request) {
    Long userId = socketService.getUserId(client);
    Long roomId = socketService.getRoomId(client);
    chatRoomFacade.sendTextMessage(
        client,
        userId,
        roomId,
        request.content());
  }

  @SocketMapping(endpoint = "/image", requestCls = SendMessageRequest.class)
  public void sendImageMessage(SocketIOClient client, @Valid SendMessageRequest request) {
    long userId = socketService.getUserId(client);
    long roomId = socketService.getRoomId(client);
    chatRoomFacade.sendImageMessage(
        client,
        userId,
        roomId,
        request.content());
  }

  @SocketMapping(endpoint = "/notice", requestCls = SendMessageRequest.class)
  public void sendNoticeMessage(SocketIOClient client, @Valid SendMessageRequest request) {
    long userId = socketService.getUserId(client);
    long roomId = socketService.getRoomId(client);
    chatRoomFacade.sendNoticeMessage(
        client,
        userId,
        roomId,
        request.content());
  }

  @SocketMapping(endpoint = "/receive", requestCls = ReceiveMessageRequest.class)
  public void receiveMessage(SocketIOClient client, @Valid ReceiveMessageRequest request) {
    long userId = socketService.getUserId(client);
    long roomId = socketService.getRoomId(client);
    chatRoomFacade.receiveMessage(
        userId,
        roomId,
        request.messageId());
  }

  @GetMapping("/room/{roomId}/message")
  public ResponseEntity<GetMessageListResponse> getMessages(
      @CurrentUserId long userId,
      @PathVariable long roomId,
      @RequestParam(required = false) Long lastMessageId
  ) {
    var response = GetMessageListResponse.of(
        chatRoomFacade.getMessages(userId, roomId, lastMessageId));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/room/{roomId}/message/recent")
  public ResponseEntity<MessageIdResponse> getRecentMessages(
      @CurrentUserId long userId,
       @PathVariable long roomId
  ) {
    var response = MessageIdResponse.from(
        chatRoomFacade.getRecentMessageId(userId, roomId));
    return ResponseEntity.ok(response);
  }
}