package org.crops.fitserver.global.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

  @Transactional
  public void sendMessage(
      SocketIOClient senderClient,
      String eventName,
      SocketResponse message) {
    var roomId = senderClient.getHandshakeData().getSingleUrlParam("roomId");
    senderClient
        .getNamespace()
        .getRoomOperations(roomId)
        .getClients()
        .forEach(client ->
            sendMessageToOtherClient(
                senderClient,
                client,
                eventName,
                message)
        );
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