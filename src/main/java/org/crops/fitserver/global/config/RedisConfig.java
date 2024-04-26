package org.crops.fitserver.global.config;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.*;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.mq.MessagePublisher;
import org.crops.fitserver.global.mq.RedisMessagePublisher;
import org.crops.fitserver.global.mq.RedisMessageSubscriber;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@EnableCaching
public class RedisConfig {

  private final RedisProperties redisProperties;

  private final ObjectMapper objectMapper;

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
  public RedisMessageListenerContainer redisContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory());
    container.addMessageListener(messageListener(), domainTopic());
    return container;
  }

  @Bean
  public MessageListenerAdapter messageListener() {
    return new MessageListenerAdapter(new RedisMessageSubscriber(objectMapper));
  }

  @Bean
  public MessagePublisher redisPublisher() {
    return new RedisMessagePublisher(redisTemplate(), domainTopic());
  }

  @Bean
  public ChannelTopic domainTopic() {
    return new ChannelTopic("domain");
  }


  @Bean
  public ChannelTopic chatTopic() {
    return new ChannelTopic("chat");
  }
}