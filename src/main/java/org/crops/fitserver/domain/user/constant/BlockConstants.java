package org.crops.fitserver.domain.user.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockConstants {

  public enum BlockStatus {
    BLOCKED, UNBLOCKED, UNBLOCKED_BY_ADMIN
  }

  public static final int DEFAULT_BLOCK_DAYS = 30;
  public static final int REPORT_MIN_COUNT = 5;
}
