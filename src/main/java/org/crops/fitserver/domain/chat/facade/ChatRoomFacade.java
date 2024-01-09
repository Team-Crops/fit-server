package org.crops.fitserver.domain.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;
import org.crops.fitserver.domain.chat.domain.MessageType;

public interface ChatRoomFacade {

  void sendMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      MessageType messageType,
      String content);
}
