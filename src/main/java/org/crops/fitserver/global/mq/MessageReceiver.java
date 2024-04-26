package org.crops.fitserver.global.mq;

import org.crops.fitserver.global.mq.dto.Message;

public interface MessageReceiver<T extends Message> {

  void onEvent(T message);
}
