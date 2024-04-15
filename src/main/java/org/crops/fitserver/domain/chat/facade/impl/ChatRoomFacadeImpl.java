package org.crops.fitserver.domain.chat.facade.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {

  private final ChatRoomService chatRoomService;
  private final MessageService messageService;
  private final UserRepository userRepository;

  @Override
  public void sendTextMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      String content) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, MessageType.TEXT, content);
    messageService.sendTextMessage(client, message);
  }

  @Override
  public void sendImageMessage(SocketIOClient client, Long userId, Long roomId, String imageUrl) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, MessageType.IMAGE, imageUrl);
    messageService.sendImageMessage(client, message);
  }

  @Override
  public void sendNoticeMessage(SocketIOClient client, Long userId, Long roomId, String notice) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, MessageType.NOTICE, notice);
    messageService.sendNoticeMessage(client, message);
  }
}
