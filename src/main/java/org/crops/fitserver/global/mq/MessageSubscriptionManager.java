package org.crops.fitserver.global.mq;

import org.crops.fitserver.global.mq.dto.Message;
import org.springframework.data.redis.connection.MessageListener;

public interface MessageSubscriptionManager<T extends Message> extends MessageListener {

  void subscribe(MessageReceiver<T> messageReceiver);
}
