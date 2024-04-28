package org.crops.fitserver.domain.chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import org.crops.fitserver.domain.chat.domain.Message;

public interface MessageService {

  Message getById(long messageId);

  void sendTextMessage(
      SocketIOClient client,
      Message message);

  void sendImageMessage(
      SocketIOClient client,
      Message message);

  void sendNoticeMessage(
      SocketIOClient client,
      Message message);
}