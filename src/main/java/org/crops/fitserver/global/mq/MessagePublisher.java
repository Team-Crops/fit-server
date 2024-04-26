package org.crops.fitserver.global.mq;

public interface MessagePublisher {
  void publish(String message);
}
