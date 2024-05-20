package org.crops.fitserver.global.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.mq.dto.Message;
import org.crops.fitserver.global.mq.dto.Report;
import org.crops.fitserver.global.mq.impl.RedisMessagePublisher;
import org.crops.fitserver.global.mq.impl.RedisMessageSubscriptionManager;
import org.crops.fitserver.global.socket.service.MessageResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class RedisPubSubBeanRegistry {

  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, Object> redisTemplate;
  private final List<MessageReceiver<? extends Message>> messageReceivers;


  @Bean
  public MessageSubscriptionManager<Report> reportMessageSubscriptionManager() {
    var targetReceivers = getMessageReceivers(Report.class);
    return new RedisMessageSubscriptionManager<>(redisTemplate, objectMapper, targetReceivers, Report.class);
  }

  @Bean
  public MessageSubscriptionManager<MessageResponse> MessageResponseMessageSubscriptionManager() {
    var targetReceivers = getMessageReceivers(MessageResponse.class);
    return new RedisMessageSubscriptionManager<>(redisTemplate, objectMapper, targetReceivers, MessageResponse.class);
  }

  @Bean
  public MessagePublisher<Report> reportMessagePublisher() {
    return new RedisMessagePublisher<>(redisTemplate, Report.class);
  }

  @Bean
  public MessagePublisher<MessageResponse> MessageResponseMessagePublisher() {
    return new RedisMessagePublisher<>(redisTemplate, MessageResponse.class);
  }

  private <T extends Message> List<MessageReceiver<T>> getMessageReceivers(
      Class<T> clazz) {
    return messageReceivers.stream().filter(
            messageReceiver -> (((ParameterizedType) messageReceiver.getClass()
                .getGenericInterfaces()[0]).getActualTypeArguments()[0]) == clazz)
        .map(messageReceiver -> (MessageReceiver<T>) messageReceiver)
        .toList();
  }
}
