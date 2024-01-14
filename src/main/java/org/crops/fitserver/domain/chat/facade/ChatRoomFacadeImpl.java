package org.crops.fitserver.domain.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.chat.service.provider.MessageServiceProvider;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {

  private final ChatRoomService chatRoomService;
  private final UserRepository userRepository;
  private final MessageServiceProvider messageServiceProvider;

  @Override
  public void sendMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      MessageType messageType,
      String content) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, messageType, content);
    var messageService = messageServiceProvider.getService(messageType);
    messageService.sendMessage(client, message);
  }
}
