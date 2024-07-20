package org.crops.fitserver.domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BackgroundStatus {
  //학력관련: 고등학교 졸업, 대학교 1학년, 2학년, 3학년, 4학년, 대학원생, 휴학생
  //경력관련: 인턴/계약직, 1년 미만, 1~3년, 4~7년, 8~10년, 10년 이상

  HIGH_SCHOOL_GRADUATE(BackgroundType.EDUCATION),
  UNIVERSITY_FRESHMAN(BackgroundType.EDUCATION),
  UNIVERSITY_SOPHOMORE(BackgroundType.EDUCATION),
  UNIVERSITY_JUNIOR(BackgroundType.EDUCATION),
  UNIVERSITY_SENIOR(BackgroundType.EDUCATION),
  GRADUATE_STUDENT(BackgroundType.EDUCATION),
  LEAVE_OF_ABSENCE_STUDENT(BackgroundType.EDUCATION),
  WORKER_CONTRACT_WORKER(BackgroundType.CAREER),
  WORKER_LESS_THAN_ONE_YEAR(BackgroundType.CAREER),
  WORKER_ONE_TO_THREE_YEARS(BackgroundType.CAREER),
  WORKER_FOUR_TO_SEVEN_YEARS(BackgroundType.CAREER),
  WORKER_EIGHT_TO_TEN_YEARS(BackgroundType.CAREER),
  WORKER_OVER_TEN_YEARS(BackgroundType.CAREER);

  private final BackgroundType backgroundType;

  public enum BackgroundType {
    EDUCATION,
    CAREER
  }
}
