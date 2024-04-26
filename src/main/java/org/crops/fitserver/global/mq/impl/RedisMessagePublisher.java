package org.crops.fitserver.global.mq.impl;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.mq.MessagePublisher;
import org.crops.fitserver.global.mq.constant.Topic;
import org.crops.fitserver.global.mq.dto.Message;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisMessagePublisher<T extends Message> implements MessagePublisher<T> {

  private final RedisTemplate<String, Object> template;

  private final Class<T> clazz;

  private final Topic topic;

  public RedisMessagePublisher(RedisTemplate<String, Object> template, Class<T> clazz) {
    this.template = template;
    this.clazz = clazz;
    this.topic = Arrays.stream(Topic.values())
        .filter(t -> t.getClazz().equals(clazz)).findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public void publish(T message) {
    log.info("Publishing message: {}", message);
    template.convertAndSend(topic.getTopic(), message);
  }
}
