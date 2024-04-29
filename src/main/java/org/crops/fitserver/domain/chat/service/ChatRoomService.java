package org.crops.fitserver.domain.chat.service;

import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.domain.Slice;

public interface ChatRoomService {

  ChatRoom createChatRoom();

  ChatRoom getById(Long roomId);

  void validateUserInRoom(User user, ChatRoom room);

  Slice<Message> getBeforeMessagesByPaging(
      long roomId,
      Long messageId,
      int size);

  Long getRecentMessageId(
      long userId,
      long chatRoomId);

  void updateLastCheckedMessage(ChatRoom room, User user);

  void updateLastCheckedMessageByMessage(ChatRoom room, User user, Message message);

  void chatRoomJoin(long chatRoomId, User user);

  void chatRoomLeave(long chatRoomId, User user);
}