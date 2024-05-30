package org.crops.fitserver.domain.chat.repository;

import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  boolean existsById(Long roomId);
}
