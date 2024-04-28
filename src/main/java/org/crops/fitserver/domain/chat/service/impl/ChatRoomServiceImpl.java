package org.crops.fitserver.domain.chat.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.domain.ChatRoomUser;
import org.crops.fitserver.domain.chat.domain.Message;
import org.crops.fitserver.domain.chat.repository.ChatRoomRepository;
import org.crops.fitserver.domain.chat.repository.ChatRoomUserRepository;
import org.crops.fitserver.domain.chat.repository.MessageRepository;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.http.CursorResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatRoomUserRepository chatRoomUserRepository;
  private final MessageRepository messageRepository;

  @Override
  public ChatRoom createChatRoom() {
    return chatRoomRepository.save(ChatRoom.newInstance());
  }

  @Override
  public ChatRoom getById(Long roomId) {
    return chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }

  @Override
  public void validateUserInRoom(User user, ChatRoom room) {
    // TODO: implement
  }

  @Override
  public Slice<Message> getBeforeMessagesByPaging(long roomId, Long messageId, int size) {
    return messageId == null ?
        messageRepository.findByChatRoomIdOrderByIdDesc(roomId, PageRequest.of(0, size)) :
        messageRepository.findByRoomIdLessThanIdOrderByIdDesc(roomId, messageId,
            PageRequest.of(0, size));
  }

  @Override
  public Long getRecentMessageId(long userId, long chatRoomId) {
    return chatRoomUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
        .map(ChatRoomUser::getLastCheckedMessage)
        .map(Message::getId)
        .orElse(null);
  }

  @Override
  public void updateLastCheckedMessage(User user, ChatRoom room) {
    messageRepository
        .findLastMessageByRoomId(room.getId())
        .ifPresent(message -> updateLastCheckedMessageByMessage(room, user, message));
  }

  @Override
  public void updateLastCheckedMessageByMessage(ChatRoom room, User user, Message message) {
    var chatRoomUser = chatRoomUserRepository
        .findByUserIdAndChatRoomId(user.getId(), room.getId())
        .orElseGet(() -> ChatRoomUser.create(user, room));
    chatRoomUser.updateLastCheckedMessage(message);
    chatRoomUserRepository.save(chatRoomUser);
  }
}
