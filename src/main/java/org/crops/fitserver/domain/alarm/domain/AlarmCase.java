package org.crops.fitserver.domain.alarm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmCase {
  FAILED_MATCHING,
  STARTED_PROJECT,
  START_PROJECT,
  NEW_MATCHING_ROOM,
  PROGRESS_MATCHING,
  FORCE_OUT,
  NEW_MESSAGE_MATCHING,
  NEW_MESSAGE_PROJECT,
  REPORT,
  ;
}
