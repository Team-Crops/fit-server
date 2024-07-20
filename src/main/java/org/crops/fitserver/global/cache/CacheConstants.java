package org.crops.fitserver.global.cache;


import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheConstants {
  RANDOM_SEED(Integer.class),
  ;

  private final Class<? extends Serializable> clazz;

}
