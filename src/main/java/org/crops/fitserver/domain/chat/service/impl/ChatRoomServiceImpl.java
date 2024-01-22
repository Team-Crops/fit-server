package org.crops.fitserver.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.repository.ChatRoomRepository;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

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
  public boolean isExistById(Long roomId) {
    return chatRoomRepository.existsById(roomId);
  }
}
