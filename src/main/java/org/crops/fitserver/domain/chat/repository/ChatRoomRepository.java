package org.crops.fitserver.domain.chat.repository;

import java.util.Optional;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.springframework.data.repository.Repository;

public interface ChatRoomRepository extends Repository<ChatRoom, Long> {

  ChatRoom save(ChatRoom chatRoom);

  Optional<ChatRoom> findById(Long roomId);

  boolean existsById(Long roomId);
}
