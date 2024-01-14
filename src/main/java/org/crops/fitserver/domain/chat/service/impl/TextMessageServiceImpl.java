package org.crops.fitserver.domain.chat.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.controller.dto.response.TextMessageResponse;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.repository.MessageRepository;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextMessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final SocketService socketService;

  @Override
  public boolean support(MessageType messageType) {
    return messageType == MessageType.TEXT;
  }

  @Override
  public Message sendMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = TextMessageResponse.from(message);
    socketService.sendMessage(client, GET_MESSAGE_EVENT_NAME, response);
    return message;
  }
}
