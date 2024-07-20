package org.crops.fitserver.global.http;

import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.springframework.core.convert.converter.Converter;

public class SocailPlatformRequestConverter implements Converter<String, SocialPlatform> {

  @Override
  public SocialPlatform convert(String socialPlatform) {
    return SocialPlatform.of(socialPlatform);
  }
}
