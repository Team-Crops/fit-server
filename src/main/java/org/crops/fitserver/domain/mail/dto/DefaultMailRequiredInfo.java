package org.crops.fitserver.domain.mail.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultMailRequiredInfo implements MailRequiredInfo {
  private final String nickname;

  public static DefaultMailRequiredInfo of(String nickname) {
    return DefaultMailRequiredInfo.builder()
      .nickname(nickname)
      .build();
  }

  public String replace(String content) {
    return content
      .replace("${nickname}", nickname);
  }

}
