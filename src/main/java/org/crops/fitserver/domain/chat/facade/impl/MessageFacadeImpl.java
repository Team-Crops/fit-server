package org.crops.fitserver.domain.chat.facade.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.dto.MessageDto;
import org.crops.fitserver.domain.chat.facade.MessageFacade;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.chat.service.MessageService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageFacadeImpl implements MessageFacade {

  private final ChatRoomService chatRoomService;
  private final MessageService messageService;

  private static final int DEFAULT_PAGE_SIZE = 10;

  @Override
  public List<MessageDto> getMessages(long userId, long roomId, int page) {
    chatRoomService.validateUserInRoom(userId, roomId);

    return messageService
        .getMessages(roomId, page, DEFAULT_PAGE_SIZE)
        .stream()
        .map(MessageDto::from)
        .collect(Collectors.toList());
  }
}