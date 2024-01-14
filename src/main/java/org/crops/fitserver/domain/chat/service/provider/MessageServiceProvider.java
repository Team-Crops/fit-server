package org.crops.fitserver.domain.chat.service.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageServiceProvider {

  private final List<MessageService> messageServices;

  public MessageService getService(MessageType messageType) {
    for (MessageService messageService : messageServices) {
      if (messageService.support(messageType)) {
        return messageService;
      }
    }
    throw new BusinessException(ErrorCode.UNSUPPORTED_MESSAGE_TYPE_EXCEPTION);
  }
}
