package org.crops.fitserver.domain.chat.repository;

import org.crops.fitserver.domain.chat.domain.Message;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

  Message save(Message message);
}
