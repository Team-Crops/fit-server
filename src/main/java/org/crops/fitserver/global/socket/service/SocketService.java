package org.crops.fitserver.global.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.socket.SocketProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

  private final SocketProperty socketProperty;

  @Transactional
  public void sendMessage(
      SocketIOClient senderClient,
      String eventName,
      SocketResponse message) {
    var roomId = String.valueOf(getRoomId(senderClient));
    senderClient
        .getNamespace()
        .getRoomOperations(roomId)
        .getClients()
        .forEach(client ->
            sendMessageToOtherClient(
                senderClient,
                client,
                eventName,
                message));
  }

  public void setUserId(SocketIOClient socketIOClient, Long userId) {
    socketIOClient.set(socketProperty.getUserKey(), userId);
  }

  public void setRoomId(SocketIOClient socketIOClient, Long roomId) {
    socketIOClient.set(socketProperty.getRoomKey(), roomId);
  }

  public Long getRoomId(SocketIOClient socketIOClient) {
    var roomId = (Long) socketIOClient.get(socketProperty.getRoomKey());
    if (Objects.isNull(roomId)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }
    return roomId;
  }

  public Long getUserId(SocketIOClient socketIOClient) {
    var userId = (Long) socketIOClient.get(socketProperty.getUserKey());
    if (Objects.isNull(userId)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }
    return userId;
  }

  private static void sendMessageToOtherClient(
      SocketIOClient senderClient,
      SocketIOClient client,
      String eventName,
      SocketResponse message) {
    if (!Objects.equals(client.getSessionId(), senderClient.getSessionId())) {
      client.sendEvent(eventName, message);
    }
  }
}