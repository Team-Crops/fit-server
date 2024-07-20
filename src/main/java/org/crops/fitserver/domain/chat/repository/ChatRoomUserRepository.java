package org.crops.fitserver.domain.chat.repository;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.chat.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

  Optional<ChatRoomUser> findByUserIdAndChatRoomId(long userId, long chatRoomId);

  boolean existsByUserIdAndChatRoomId(long userId, long chatRoomId);

  @Query("select cru from ChatRoomUser cru "
      + "join fetch cru.user u "
      + "where cru.chatRoom.id = :chatRoomId")
  List<ChatRoomUser> findByChatRoomIdWithUser(long chatRoomId);
}
