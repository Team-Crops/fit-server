package org.crops.fitserver.domain.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;
import java.util.List;
import org.crops.fitserver.domain.chat.dto.MessageDto;
import org.crops.fitserver.global.http.CursorResult;

public interface ChatRoomFacade {

  void sendTextMessage(
      SocketIOClient client,
      long userId,
      long roomId,
      String content);

  void sendImageMessage(
      SocketIOClient client,
      long userId,
      long roomId,
      String imageUrl);

  void sendNoticeMessage(
      SocketIOClient client,
      long userId,
      long roomId,
      String notice);

  void receiveMessage(
      long userId,
      long roomId,
      long messageId);

  CursorResult<MessageDto> getMessages(
      long userId,
      long roomId,
      Long lastMessageId);

  Long getRecentMessageId(long userId, long roomId);
}
