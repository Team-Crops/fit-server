package org.crops.fitserver.global.socket.service;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.socket.SocketProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketService {

  private final SocketProperty socketProperty;
  private final Map<Long, Set<SocketIOClient>> clientsMap = new HashMap<>();

  @Transactional
  public void sendMessage(
      SocketIOClient senderClient,
      Object message) {
    var roomId = String.valueOf(getRoomId(senderClient));
    senderClient
        .getNamespace()
        .getRoomOperations(roomId)
        .getClients()
        .forEach(client ->
            client.sendEvent(socketProperty.getGetMessageEvent(), message));
  }

  public void sendNotice(
      Long roomId,
      Object message) {
    if (!clientsMap.containsKey(roomId)) {
      log.warn("Room not found. roomId: {}", roomId);
      return;
    }
    clientsMap.get(roomId)
        .forEach(client -> client.sendEvent(socketProperty.getGetMessageEvent(), message));
  }

  public void addClients(Long roomId, SocketIOClient client) {
    if (!clientsMap.containsKey(roomId)) {
      clientsMap.put(roomId, new HashSet<>());
    }
    clientsMap.get(roomId).add(client);
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