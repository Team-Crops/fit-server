package org.crops.fitserver.global.mq.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.mq.MessageReceiver;
import org.crops.fitserver.global.mq.MessageSubscriptionManager;
import org.crops.fitserver.global.mq.constant.Topic;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisMessageSubscriptionManager<T extends org.crops.fitserver.global.mq.dto.Message> implements
    MessageSubscriptionManager<T> {

  private final RedisTemplate<String, Object> template;
  private final ObjectMapper objectMapper;

  private final Set<MessageReceiver<T>> messageReceivers;

  @Getter
  private final Topic topic;
  private final Class<T> clazz;

  public RedisMessageSubscriptionManager(RedisTemplate<String, Object> template,
      ObjectMapper objectMapper,
      List<MessageReceiver<T>> messageReceivers,
      Class<T> clazz) {
    this.template = template;
    this.objectMapper = objectMapper;
    this.messageReceivers = new HashSet<>(messageReceivers);
    this.clazz = clazz;
    this.topic = Arrays.stream(Topic.values())
        .filter(t -> t.getClazz().equals(clazz)).findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String publishMessage = template
          .getStringSerializer().deserialize(message.getBody());

      T value = objectMapper.readValue(publishMessage, clazz);
      log.debug("Received message: {}", value);

      messageReceivers.forEach(messageReceiver -> messageReceiver.onEvent(value));
    } catch (IOException e) {
      log.error("Error processing message", e);
    }
  }

  @Override
  public void subscribe(MessageReceiver<T> messageReceiver) {
    messageReceivers.add(messageReceiver);
  }

}
