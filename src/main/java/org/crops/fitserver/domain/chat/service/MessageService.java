package org.crops.fitserver.domain.chat.service;

import org.crops.fitserver.domain.chat.domain.Message;

public interface MessageService {

  Message getById(long messageId);

  Message saveTextMessage(Message message);

  Message saveImageMessage(Message message);

  Message saveNoticeMessage(Message message);
}