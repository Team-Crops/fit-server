package org.crops.fitserver.domain.chat.repository;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.chat.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

  Optional<Message> findById(long messageId);

  Message save(Message message);

  Slice<Message> findByChatRoomIdOrderByIdDesc(long roomId, Pageable pageable);

  @Query("select m "
      + "from Message m "
      + "where m.chatRoom.id = :roomId "
      + "and m.id < :messageId "
      + "order by m.id desc")
  Slice<Message> findByRoomIdLessThanIdOrderByIdDesc(long roomId, long messageId, Pageable pageable);

  @Query("select m "
      + "from Message m "
      + "where m.chatRoom.id = :roomId "
      + "order by m.id desc "
      + "limit 1")
  Optional<Message> findLastMessageByRoomId(long roomId);
}