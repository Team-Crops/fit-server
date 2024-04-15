package org.crops.fitserver.domain.chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import java.util.List;
import org.crops.fitserver.domain.chat.domain.Message;

public interface MessageService {

  void sendTextMessage(
      SocketIOClient client,
      Message message);

  void sendImageMessage(
      SocketIOClient client,
      Message message);

  void sendNoticeMessage(
      SocketIOClient client,
      Message message);

  List<Message> getMessages(long roomId, int page, int size);
}