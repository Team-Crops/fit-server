package org.crops.fitserver.domain.chat.facade.impl;

import static java.util.stream.Collectors.*;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.alarm.domain.AlarmCase;
import org.crops.fitserver.domain.alarm.service.AlarmService;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.domain.ChatRoomUser;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.domain.chat.repository.ChatRoomUserRepository;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.domain.project.repository.ProjectRepository;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.service.UserService;
import org.crops.fitserver.global.http.PageResult;
import org.crops.fitserver.global.socket.service.MessageResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {

  private final ChatRoomService chatRoomService;
  private final MessageService messageService;
  private final UserService userService;
  private final AlarmService alarmService;
  private final ProjectRepository projectRepository;
  private final ChatRoomUserRepository chatRoomUserRepository;

  private static final int DEFAULT_PAGE_SIZE = 10;

  @Override
  @Transactional
  public MessageResponse sendTextMessage(SocketIOClient client, long userId, long roomId, String content) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    var message = messageService.saveTextMessage(
        Message.newInstance(user, room, MessageType.TEXT, content));
    chatRoomService.validateUserInRoom(user, room);
    sendMessageAlarm(room, user);
    chatRoomService.updateLastCheckedMessageByMessage(room, user, message);
    return MessageResponse.from(message);
  }

  private void sendMessageAlarm(ChatRoom chatRoom, User sender) {
    final AlarmCase alarmCase = projectRepository.existsByChatRoomId(chatRoom.getId()) ?
        AlarmCase.NEW_MESSAGE_PROJECT :
        AlarmCase.NEW_MESSAGE_MATCHING;
    chatRoomUserRepository.findByChatRoomIdWithUser(chatRoom.getId())
        .stream()
        .map(ChatRoomUser::getUser)
        .filter(u -> !u.equals(sender))
        .forEach(u -> alarmService.sendAlarmIfNotRead(u, alarmCase));
  }

  @Override
  @Transactional
  public MessageResponse sendImageMessage(SocketIOClient client, long userId, long roomId, String imageUrl) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    var message = messageService.saveImageMessage(
        Message.newInstance(user, room, MessageType.IMAGE, imageUrl));
    chatRoomService.validateUserInRoom(user, room);
    chatRoomService.updateLastCheckedMessageByMessage(room, user, message);
    return MessageResponse.from(message);
  }

  @Override
  @Transactional
  public void receiveMessage(long userId, long roomId, long messageId) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    var message = messageService.getById(messageId);
    chatRoomService.validateUserInRoom(user, room);
    chatRoomService.updateLastCheckedMessageByMessage(room, user, message);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResult<MessageResponse> getMessages(long userId, long roomId, Long lastMessageId) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    Slice<Message> slice = chatRoomService.getBeforeMessagesByPaging(
        room.getId(), lastMessageId, DEFAULT_PAGE_SIZE);
    chatRoomService.validateUserInRoom(user, room);
    if (lastMessageId == null) {
      chatRoomService.updateLastCheckedMessage(room, user);
    }
    return PageResult.of(
        slice.getContent()
            .stream()
            .map(MessageResponse::from)
            .collect(toList()),
        slice.hasNext());
  }

  @Override
  @Transactional(readOnly = true)
  public Long getRecentMessageId(long userId, long roomId) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    chatRoomService.validateUserInRoom(user, room);
    return chatRoomService.getRecentMessageId(user.getId(), room.getId());
  }
}
