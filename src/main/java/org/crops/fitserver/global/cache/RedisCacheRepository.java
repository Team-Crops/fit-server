package org.crops.fitserver.global.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RedisCacheRepository implements CacheRepository {

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;


  @Override
  public <T extends Serializable> T get(String key, Class<T> clazz) {
    try {
      return objectMapper.readValue(redisTemplate.opsForValue().get(key), clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Serializable> T get(String key, Class<T> clazz, Supplier<T> supplier) {
    var value = redisTemplate.opsForValue().get(key);
    try {
      if (value == null) {
        var valueObject = Objects.requireNonNull(supplier.get());
        value = objectMapper.writeValueAsString(valueObject);

        redisTemplate.opsForValue().set(key, value);
        return valueObject;
      }

      return objectMapper.readValue(value, clazz);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Serializable> void set(String key, T valueObject) {
    try {
      redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(valueObject));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Serializable> void set(String key, T valueObject, int timeout, TimeUnit timeUnit) {
    try {
      redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(valueObject), timeout, timeUnit);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
