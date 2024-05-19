package org.crops.fitserver.global.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface CacheRepository {

  <T> T get(String key);

  <T> T get(String key, Supplier<T> supplier);

  <T> void set(String key, T data);

  <T> void set(String key, T data, int timeout, TimeUnit timeUnit);

}
