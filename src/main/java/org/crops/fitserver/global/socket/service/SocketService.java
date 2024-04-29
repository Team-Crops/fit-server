package org.crops.fitserver.global.socket.service;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
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
  private final Map<Long, BroadcastOperations> roomOperationsMap = new HashMap<>();

  @Transactional
  public void sendMessage(
      SocketIOClient senderClient,
      MessageResponse message) {
    var roomId = String.valueOf(getRoomId(senderClient));
    senderClient
        .getNamespace()
        .getRoomOperations(roomId)
        .getClients()
        .forEach(client ->
            sendMessageToOtherClient(
                senderClient,
                client,
                message));
  }

  public void sendNotice(
      Long roomId,
      MessageResponse message) {
    roomOperationsMap
        .get(roomId)
        .getClients()
        .forEach(client -> client.sendEvent(socketProperty.getGetMessageEvent(), message));
  }

  public void addRoomOperations(Long roomId, BroadcastOperations broadcastOperations) {
    if (!roomOperationsMap.containsKey(roomId)) {
      roomOperationsMap.put(roomId, broadcastOperations);
    }
  }

  public Long getRoomId(SocketIOClient socketIOClient) {
    var roomId = socketIOClient.<Long>get(socketProperty.getRoomKey());
    if (Objects.isNull(roomId)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }
    return roomId;
  }

  public Long getUserId(SocketIOClient socketIOClient) {
    var userId = socketIOClient.<Long>get(socketProperty.getUserKey());
    if (Objects.isNull(userId)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }
    return userId;
  }

  private void sendMessageToOtherClient(
      SocketIOClient senderClient,
      SocketIOClient client,
      MessageResponse message) {
    if (!Objects.equals(senderClient.getSessionId(), client.getSessionId())) {
      client.sendEvent(socketProperty.getGetMessageEvent(), message);
    }
  }
}