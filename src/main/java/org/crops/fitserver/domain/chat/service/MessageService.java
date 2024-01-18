package org.crops.fitserver.domain.chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import org.crops.fitserver.domain.chat.domain.Message;

public interface MessageService {

  Message sendTextMessage(
      SocketIOClient client,
      Message message);

  Message sendImageMessage(
      SocketIOClient client,
      Message message);

  Message sendNoticeMessage(
      SocketIOClient client,
      Message message);
}