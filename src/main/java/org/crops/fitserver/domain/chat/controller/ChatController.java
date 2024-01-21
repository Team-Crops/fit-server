package org.crops.fitserver.domain.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.controller.dto.request.SendMessageRequest;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.global.annotation.SocketController;
import org.crops.fitserver.global.annotation.SocketMapping;
import org.crops.fitserver.global.socket.service.SocketService;

@Slf4j
@SocketController
@RequiredArgsConstructor
public class ChatController {

  private final ChatRoomFacade chatRoomFacade;
  private final SocketService socketService;

  @SocketMapping(endpoint = "/app/chat/text", requestCls = SendMessageRequest.class)
  public void sendTextMessage(SocketIOClient client, @Valid SendMessageRequest request) {
    Long userId = socketService.getUserId(client);
    Long roomId = socketService.getRoomId(client);
    chatRoomFacade.sendTextMessage(
        client,
        userId,
        roomId,
        request.content());
  }

  @SocketMapping(endpoint = "/app/chat/image", requestCls = SendMessageRequest.class)
  public void sendImageMessage(SocketIOClient client, @Valid SendMessageRequest request) {
    Long userId = socketService.getUserId(client);
    Long roomId = socketService.getRoomId(client);
    chatRoomFacade.sendImageMessage(
        client,
        userId,
        roomId,
        request.content());
  }

  @SocketMapping(endpoint = "/app/chat/notice", requestCls = SendMessageRequest.class)
  public void sendNoticeMessage(SocketIOClient client, @Valid SendMessageRequest request) {
    Long userId = socketService.getUserId(client);
    Long roomId = socketService.getRoomId(client);
    chatRoomFacade.sendNoticeMessage(
        client,
        userId,
        roomId,
        request.content());
  }
}
