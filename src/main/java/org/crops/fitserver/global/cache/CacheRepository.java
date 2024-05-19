package org.crops.fitserver.global.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface CacheRepository {

  <T extends Serializable> T get(String key, Class<T> clazz);

  <T extends Serializable> T get(String key, Class<T> clazz, Supplier<T> supplier);

  <T extends Serializable> void set(String key, T valueObject);

  <T extends Serializable> void set(String key, T valueObject, int timeout, TimeUnit timeUnit);

}
