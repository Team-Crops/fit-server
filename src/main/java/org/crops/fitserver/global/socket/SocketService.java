package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

  @Transactional
  public <T> void sendMessage(SocketIOClient senderClient, String eventName, T message) {
    String roomId = senderClient.getHandshakeData().getSingleUrlParam("roomId");
    for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomId).getClients()) {
      if (!client.getSessionId().equals(senderClient.getSessionId())) {
        client.sendEvent(eventName, message);
      }
    }
  }
}