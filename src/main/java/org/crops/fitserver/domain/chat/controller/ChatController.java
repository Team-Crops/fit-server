package org.crops.fitserver.domain.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.controller.dto.request.SendMessageRequest;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.global.annotation.SocketController;
import org.crops.fitserver.global.annotation.SocketMapping;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.springframework.http.HttpHeaders;

@Slf4j
@SocketController
@RequiredArgsConstructor
public class ChatController {

  private final ChatRoomFacade chatRoomFacade;
  private final HeaderTokenExtractor headerTokenExtractor;
  private final JwtResolver jwtResolver;

  @SocketMapping(endpoint = "/app/message", requestCls = SendMessageRequest.class)
  public void sendChat(SocketIOClient client, SendMessageRequest request) {
    String accessToken = headerTokenExtractor
        .extractAccessToken(client
            .getHandshakeData()
            .getHttpHeaders()
            .get(HttpHeaders.AUTHORIZATION));
    Long userId = jwtResolver.getUserIdFromAccessToken(accessToken);
    Long roomId = Long.valueOf(client.getHandshakeData().getSingleUrlParam("roomId"));
    chatRoomFacade.sendMessage(client, userId, roomId, request.messageType(), request.content());
  }
}
