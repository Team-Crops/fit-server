package org.crops.fitserver.domain.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;

public interface ChatRoomFacade {

  void sendTextMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      String content);

  void sendImageMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      String imageUrl);

  void sendNoticeMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      String notice);
}
