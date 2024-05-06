package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.service.UserService;
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
  private final UserService userService;
  private final SocketService socketService;
  private final SocketProperty socketProperty;

  @Override
  @Transactional
  public void onConnect(SocketIOClient socketIOClient) {
    User user = getUser(socketIOClient);
    ChatRoom room = getRoom(socketIOClient);
    saveUserId(socketIOClient, user.getId());
    saveRoomId(socketIOClient, room.getId());
    socketService.addRoomOperations(
        room.getId(),
        socketIOClient
            .getNamespace()
            .getRoomOperations(String.valueOf(room.getId())));
    log.info("Socket ID[{}]  User Id {} Room Id {} Connected to socket",
        socketIOClient.getSessionId().toString(), user.getId(), room.getId());
    joinRoom(socketIOClient, room);
  }

  private User getUser(SocketIOClient socketIOClient) {
    String accessToken = headerTokenExtractor
        .extractAccessToken(
            socketIOClient
                .getHandshakeData()
                .getHttpHeaders()
                .get(HttpHeaders.AUTHORIZATION));
    Long userId = jwtResolver.getUserIdFromAccessToken(accessToken);
    return userService.getById(userId);
  }

  private ChatRoom getRoom(SocketIOClient socketIOClient) {
    Long roomId = Long.valueOf(
        socketIOClient
            .getHandshakeData()
            .getSingleUrlParam("roomId"));
    validRoomId(roomId);
    return chatRoomService.getById(roomId);
  }

  private void validRoomId(Long roomId) {
    if (roomId == null) {
      throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }
  }

  private void saveUserId(SocketIOClient socketIOClient, Long userId) {
    socketIOClient.set(socketProperty.getUserKey(), userId);
  }

  private void saveRoomId(SocketIOClient socketIOClient, Long roomId) {
    socketIOClient.set(socketProperty.getRoomKey(), roomId);
  }

  private void joinRoom(SocketIOClient socketIOClient, ChatRoom room) {
    String stringRoomId = String.valueOf(room.getId());
    socketIOClient.joinRoom(stringRoomId);
  }
}
