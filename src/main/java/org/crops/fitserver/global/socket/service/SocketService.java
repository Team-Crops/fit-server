package org.crops.fitserver.global.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private final ObjectMapper objectMapper;
  private final Map<Long, Set<SocketIOClient>> clientsMap = new HashMap<>();

  @Transactional
  public void sendMessage (
      SocketIOClient senderClient,
      MessageResponse message) {
    var roomId = String.valueOf(getRoomId(senderClient));

    try {
      String stringMessage = objectMapper.writeValueAsString(message);
      senderClient
          .getNamespace()
          .getRoomOperations(roomId)
          .getClients()
          .forEach(client ->
              client.sendEvent(
                  socketProperty.getGetMessageEvent(),
                  stringMessage)
          );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendNotice(
      Long roomId,
      MessageResponse message) {
    if (!clientsMap.containsKey(roomId)) {
      log.warn("Room not found. roomId: {}", roomId);
      return;
    }
    try {
      String stringMessage = objectMapper.writeValueAsString(message);
      clientsMap.get(roomId)
          .forEach(client ->
              client.sendEvent(socketProperty.getGetMessageEvent(), stringMessage));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void addClients(SocketIOClient client) {
    Long roomId = getRoomId(client);
    if (!clientsMap.containsKey(roomId)) {
      clientsMap.put(roomId, new HashSet<>());
    }
    clientsMap.get(roomId).add(client);
  }

  public void removeClients(SocketIOClient client) {
    Long roomId = getRoomId(client);
    if (clientsMap.containsKey(roomId)) {
      clientsMap.get(roomId).remove(client);
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