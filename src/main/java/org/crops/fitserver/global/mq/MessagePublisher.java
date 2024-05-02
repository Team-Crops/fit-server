package org.crops.fitserver.global.mq;

import org.crops.fitserver.global.mq.dto.Message;

public interface MessagePublisher<T extends Message> {

  void publish(T message);
}
