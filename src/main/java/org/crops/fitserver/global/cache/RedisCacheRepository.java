package org.crops.fitserver.global.cache;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCacheRepository implements CacheRepository {

  private final RedisTemplate<String, Object> redisTemplate;


  @Override
  public <T> T get(String key) {
    return (T) redisTemplate.opsForValue().get(key);
  }

  @Override
  public <T> T get(String key, Supplier<T> supplier) {
    var value = (T)redisTemplate.opsForValue().get(key);
    if (value == null) {
      value = Objects.requireNonNull(supplier.get());
      redisTemplate.opsForValue().set(key, value);
    }
    return value;
  }

  @Override
  public <T> void set(String key, T value) {
    redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public <T> void set(String key, T value, int timeout, TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
  }
}
