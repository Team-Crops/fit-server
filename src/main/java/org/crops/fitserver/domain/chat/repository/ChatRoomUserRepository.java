package org.crops.fitserver.domain.chat.repository;

import java.util.Optional;
import org.crops.fitserver.domain.chat.domain.ChatRoomUser;
import org.springframework.data.repository.Repository;

public interface ChatRoomUserRepository extends Repository<ChatRoomUser, Long> {

  ChatRoomUser save(ChatRoomUser chatRoomUser);

  Optional<ChatRoomUser> findByUserIdAndChatRoomId(long userId, long chatRoomId);

  void delete(ChatRoomUser chatRoomUser);
}
