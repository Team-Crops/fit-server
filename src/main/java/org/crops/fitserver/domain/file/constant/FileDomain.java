package org.crops.fitserver.domain.file.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileDomain {
  PROFILE_IMAGE("/profile", false),
  PORTFOLIO("/portfolio", false),
  CHAT("/chat", true);

  private final String directory;
  private final boolean isTemporary;
}
