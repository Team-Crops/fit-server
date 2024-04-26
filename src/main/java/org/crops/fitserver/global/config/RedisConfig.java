package org.crops.fitserver.global.config;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.mq.MessageSubscriptionManager;
import org.crops.fitserver.global.mq.constant.Topic;
import org.crops.fitserver.global.mq.dto.Message;
import org.crops.fitserver.global.mq.impl.RedisMessageSubscriptionManager;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@EnableCaching
public class RedisConfig {

  private final RedisProperties redisProperties;

  private final ApplicationContext applicationContext;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(
        redisProperties.getHost(),
        redisProperties.getPort());
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
    return redisTemplate;
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    return defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(60))
        .disableCachingNullValues()
        .serializeKeysWith(
            fromSerializer(new StringRedisSerializer())
        )
        .serializeValuesWith(
            fromSerializer(new GenericJackson2JsonRedisSerializer())
        );
  }

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory());

    List<MessageSubscriptionManager> messageSubscriptionManagers = applicationContext
        .getBeansOfType(MessageSubscriptionManager.class).values().stream().toList();

    for (Topic topic : Topic.values()) {
      container.addMessageListener(messageSubscriptionManagers.stream()
              .filter(p -> ((RedisMessageSubscriptionManager<? extends Message>) p).getTopic()
                  .equals(topic)).findFirst().get(),
          topic.getChannelTopic());
    }
    return container;
  }
}