package org.crops.fitserver.domain.chat.service;

import org.crops.fitserver.domain.chat.domain.ChatRoom;

public interface ChatRoomService {

  ChatRoom createChatRoom();

  ChatRoom getById(Long roomId);
}