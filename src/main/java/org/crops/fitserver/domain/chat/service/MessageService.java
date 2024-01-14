package org.crops.fitserver.domain.chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;

public interface MessageService {

  String GET_MESSAGE_EVENT_NAME = "get_message";
  boolean support(MessageType messageType);

  Message sendMessage(
      SocketIOClient client,
      Message message);
}
