package org.crops.fitserver.domain.chat.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.repository.MessageRepository;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.global.socket.SocketProperty;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final SocketService socketService;
  private final SocketProperty socketProperty;

  @Override
  public void sendTextMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = TextMessageResponse.from(message);
    socketService.sendMessage(client, socketProperty.getGetMessageEvent(), response);
  }

  @Override
  public void sendImageMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = ImageMessageResponse.from(message);
    socketService.sendMessage(client, socketProperty.getGetMessageEvent(), response);
  }

  @Override
  public void sendNoticeMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = NoticeMessageResponse.from(message);
    socketService.sendMessage(client, socketProperty.getGetMessageEvent(), response);
  }

  @Override
  public List<Message> getMessages(long roomId, int page, int size) {
    return messageRepository.findAllByRoomId(roomId, PageRequest.of(page, size));
  }
}