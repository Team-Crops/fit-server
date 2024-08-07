package org.crops.fitserver.domain.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;
import org.crops.fitserver.global.http.PageResult;
import org.crops.fitserver.global.socket.service.MessageResponse;

public interface ChatRoomFacade {

  MessageResponse sendTextMessage(
      SocketIOClient client,
      long userId,
      long roomId,
      String content);

  MessageResponse sendImageMessage(
      SocketIOClient client,
      long userId,
      long roomId,
      String imageUrl);

  void receiveMessage(
      long userId,
      long roomId,
      long messageId);

  PageResult<MessageResponse> getMessages(
      long userId,
      long roomId,
      Long lastMessageId);

  Long getRecentMessageId(long userId, long roomId);
}
