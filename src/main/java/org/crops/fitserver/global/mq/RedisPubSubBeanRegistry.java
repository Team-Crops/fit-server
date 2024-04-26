package org.crops.fitserver.global.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.mq.dto.Report;
import org.crops.fitserver.global.mq.impl.RedisMessagePublisher;
import org.crops.fitserver.global.mq.impl.RedisMessageSubscriptionManager;
import org.crops.fitserver.global.socket.service.SocketResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubBeanRegistry {

  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, Object> redisTemplate;


  @Bean
  public MessageSubscriptionManager<Report> reportMessageSubscriptionManager() {
    return new RedisMessageSubscriptionManager<>(redisTemplate, objectMapper, Report.class);
  }

  @Bean
  public MessageSubscriptionManager<SocketResponse> socketResponseMessageSubscriptionManager() {
    return new RedisMessageSubscriptionManager<>(redisTemplate, objectMapper, SocketResponse.class);
  }

  @Bean
  public MessagePublisher<Report> reportMessagePublisher() {
    return new RedisMessagePublisher<>(redisTemplate, Report.class);
  }

  @Bean
  public MessagePublisher<SocketResponse> socketResponseMessagePublisher() {
    return new RedisMessagePublisher<>(redisTemplate, SocketResponse.class);
  }
}
