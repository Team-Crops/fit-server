package org.crops.fitserver.global.socket.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.global.annotation.ChatMessageType;

public class ChatMessageTypeValidator implements ConstraintValidator<ChatMessageType, MessageType> {

  private static final Set<MessageType> CHAT_MESSAGE_TYPES;

  static {
    CHAT_MESSAGE_TYPES = Collections.unmodifiableSet(
        EnumSet.of(
            MessageType.IMAGE,
            MessageType.TEXT));
  }

  @Override
  public boolean isValid(MessageType messageType, ConstraintValidatorContext context) {
    if (messageType == null) {
      return false;
    }
    return CHAT_MESSAGE_TYPES.contains(messageType);
  }
}
