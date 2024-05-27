package org.crops.fitserver.domain.mail.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultMailRequiredInfo implements MailRequiredInfo {
  private final String username;

  public static DefaultMailRequiredInfo of(String username) {
    return DefaultMailRequiredInfo.builder()
      .username(username)
      .build();
  }

  public String replace(String content) {
    return content
      .replace("${username}", username);
  }

}
