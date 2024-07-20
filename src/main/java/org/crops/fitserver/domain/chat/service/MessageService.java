package org.crops.fitserver.domain.chat.service;

import org.crops.fitserver.domain.chat.domain.Message;

public interface MessageService {

  Message getById(long messageId);

  Message saveMessage(Message message);
}