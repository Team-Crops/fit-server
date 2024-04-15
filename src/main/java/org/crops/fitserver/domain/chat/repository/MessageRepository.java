package org.crops.fitserver.domain.chat.repository;

import java.util.List;
import org.crops.fitserver.domain.chat.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

  Message save(Message message);

  @Query("select m "
      + "from Message m "
      + "join fetch m.user "
      + "where m.chatRoom.id = :roomId "
      + "order by m.createdAt desc")
  List<Message> findAllByRoomId(long roomId, Pageable pageable);
}
