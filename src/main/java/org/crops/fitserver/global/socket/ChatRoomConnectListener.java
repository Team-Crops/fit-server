package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomConnectListener implements ConnectListener {

  private final HeaderTokenExtractor headerTokenExtractor;
  private final JwtResolver jwtResolver;
  private final ChatRoomService chatRoomService;
  private final SocketService socketService;

  @Override
  @Transactional
  public void onConnect(SocketIOClient socketIOClient) {
    Long roomId = getRoomId(socketIOClient);
    Long userId = getUserId(socketIOClient);
    socketService.setUserId(socketIOClient, userId);
    socketService.setRoomId(socketIOClient, roomId);
    log.info("Socket ID[{}]  Connected to socket", socketIOClient.getSessionId().toString());
    socketIOClient.joinRoom(String.valueOf(roomId));
  }

  private Long getUserId(SocketIOClient socketIOClient) {
    String accessToken = headerTokenExtractor
        .extractAccessToken(
            socketIOClient
                .getHandshakeData()
                .getHttpHeaders()
                .get(HttpHeaders.AUTHORIZATION));
    return jwtResolver.getUserIdFromAccessToken(accessToken);
  }

  private Long getRoomId(SocketIOClient socketIOClient) {
    Long roomId = Long.valueOf(
        socketIOClient
            .getHandshakeData()
            .getSingleUrlParam("roomId"));
    validRoomId(roomId);
    return roomId;
  }

  private void validRoomId(Long roomId) {
    if (roomId == null) {
      throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }
    chatRoomService.getById(roomId);
  }
}
