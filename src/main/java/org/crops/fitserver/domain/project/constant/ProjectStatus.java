package org.crops.fitserver.domain.project.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
  PROJECT_IN_PROGRESS(1),
  PROJECT_COMPLETE(2),
  ;

  private final int step; // 정렬용도
}
