package org.crops.fitserver.domain.chat.service;

import org.crops.fitserver.domain.chat.domain.Message;

public interface MessageService {

  Message save(Message message);
}
