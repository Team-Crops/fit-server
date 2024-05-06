package org.crops.fitserver.domain.chat.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.repository.MessageRepository;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final SocketService socketService;

  @Override
  public Message getById(long messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  public void sendTextMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = TextMessageResponse.from(message);
    socketService.sendMessage(client, response);
  }

  @Override
  public void sendImageMessage(
      SocketIOClient client,
      Message message) {
    messageRepository.save(message);
    var response = ImageMessageResponse.from(message);
    socketService.sendMessage(client, response);
  }

  @Override
  public void sendNoticeMessage(Message message) {
    messageRepository.save(message);
    var response = NoticeMessageResponse.from(message);
    socketService.sendNotice(message.getChatRoom().getId(), response);
  }
}