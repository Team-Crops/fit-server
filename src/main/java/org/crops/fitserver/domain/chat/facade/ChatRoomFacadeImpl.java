package org.crops.fitserver.domain.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.controller.dto.response.StringMessageResponse;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.socket.service.SocketResponse;
import org.crops.fitserver.global.socket.service.SocketService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {

  private final ChatRoomService chatRoomService;
  private final MessageService messageService;
  private final SocketService socketService;
  private final UserRepository userRepository;

  private final static String GET_MESSAGE_EVENT_NAME = "get_message";

  @Override
  public void sendMessage(
      SocketIOClient client,
      Long userId,
      Long roomId,
      MessageType messageType,
      String content) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    ChatRoom room = chatRoomService.getById(roomId);
    Message message = Message.newInstance(user, room, messageType, content);
    messageService.save(message);

    var response = SocketResponse.of(
        user.getId(),
        user.getUserName(),
        user.getProfileImageUrl(),
        messageType,
        new StringMessageResponse(content));
    socketService.sendMessage(client, GET_MESSAGE_EVENT_NAME, response);
  }
}
