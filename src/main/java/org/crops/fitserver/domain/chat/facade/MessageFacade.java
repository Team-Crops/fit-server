package org.crops.fitserver.domain.chat.facade;

import java.util.List;
import org.crops.fitserver.domain.chat.dto.MessageDto;

public interface MessageFacade {

  List<MessageDto> getMessages(long userId, long roomId, int page);
}
