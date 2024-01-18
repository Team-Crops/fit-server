package org.crops.fitserver.global.socket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("socket")
public class SocketProperty {

  private Integer port;
  private String userKey;
  private String roomKey;
}
