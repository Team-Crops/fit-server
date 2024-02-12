package org.crops.fitserver.global.socket.service;

import lombok.Getter;
import org.crops.fitserver.domain.chat.domain.MessageType;

@Getter
public abstract class SocketResponse {

  protected MessageType messageType;
}