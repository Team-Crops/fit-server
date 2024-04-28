package org.crops.fitserver.domain.chat.facade.impl;

import com.corundumstudio.socketio.SocketIOClient;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.dto.MessageDto;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.domain.user.service.UserService;
import org.crops.fitserver.global.http.CursorResult;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {

  private final ChatRoomService chatRoomService;
  private final MessageService messageService;
  private final UserService userService;

  private static final int DEFAULT_PAGE_SIZE = 10;

  @Override
  @Transactional
  public void sendTextMessage(SocketIOClient client, long userId, long roomId, String content) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, MessageType.TEXT, content);
    chatRoomService.validateUserInRoom(user, room);
    messageService.sendTextMessage(client, message);
    chatRoomService.updateLastCheckedMessageByMessage(room, user, message);
  }

  @Override
  @Transactional
  public void sendImageMessage(SocketIOClient client, long userId, long roomId, String imageUrl) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, MessageType.IMAGE, imageUrl);
    chatRoomService.validateUserInRoom(user, room);
    messageService.sendImageMessage(client, message);
    chatRoomService.updateLastCheckedMessageByMessage(room, user, message);
  }

  @Override
  @Transactional
  public void sendNoticeMessage(SocketIOClient client, long userId, long roomId, String notice) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    var message = Message.newInstance(user, room, MessageType.NOTICE, notice);
    chatRoomService.validateUserInRoom(user, room);
    messageService.sendNoticeMessage(client, message);
    chatRoomService.updateLastCheckedMessageByMessage(room, user, message);
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
  public CursorResult<MessageDto> getMessages(long userId, long roomId, Long lastMessageId) {
    var user = userService.getById(userId);
    var room = chatRoomService.getById(roomId);
    chatRoomService.validateUserInRoom(user, room);
    Slice<Message> slice = chatRoomService.getBeforeMessagesByPaging(
        room.getId(), lastMessageId, DEFAULT_PAGE_SIZE);
    return CursorResult.of(
        slice.getContent()
            .stream()
            .map(MessageDto::from)
            .collect(Collectors.toList()),
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
