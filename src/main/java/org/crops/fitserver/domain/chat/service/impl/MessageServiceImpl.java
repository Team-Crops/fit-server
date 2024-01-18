package org.crops.fitserver.domain.chat.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.controller.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.controller.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.controller.dto.response.TextMessageResponse;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.repository.MessageRepository;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final SocketService socketService;

  String GET_MESSAGE_EVENT_NAME = "get_message";

  @Override
  public Message sendTextMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = TextMessageResponse.from(message);
    socketService.sendMessage(client, GET_MESSAGE_EVENT_NAME, response);
    return message;
  }

  @Override
  public Message sendImageMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = ImageMessageResponse.from(message);
    socketService.sendMessage(client, GET_MESSAGE_EVENT_NAME, response);
    return message;
  }

  @Override
  public Message sendNoticeMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = NoticeMessageResponse.from(message);
    socketService.sendMessage(client, GET_MESSAGE_EVENT_NAME, response);
    return message;
  }
}