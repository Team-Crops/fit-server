package org.crops.fitserver.domain.matching.constant;

import java.util.List;

public enum MatchingStatus {
  WAITING,
  MATCHED,
  ACCEPTED,
  EXPIRED,
  CANCELED,
  FORCED_OUT,
  EXITED,
  COMPLETED,

  ;

  public static List<MatchingStatus> getActiveStatusList() {
    return List.of(WAITING, MATCHED, ACCEPTED);
  }
}
