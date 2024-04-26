package org.crops.fitserver.global.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@RequiredArgsConstructor
@Slf4j
public class RedisMessagePublisher implements MessagePublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  private final ChannelTopic topic;

  @Override
  public void publish(String message) {
    log.info("Publishing message size: {}", message.length());
    log.info("Publishing message: {}", message);
    redisTemplate.convertAndSend(topic.getTopic(), message);
  }
}
