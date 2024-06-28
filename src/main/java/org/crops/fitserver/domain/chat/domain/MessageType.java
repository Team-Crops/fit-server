package org.crops.fitserver.domain.chat.domain;

import lombok.Getter;

@Getter
public enum MessageType {

  NOTICE, TEXT, IMAGE, JOIN, EXIT, READY, CANCEL_READY, COMPLETE, FORCED_OUT
}