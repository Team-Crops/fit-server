package org.crops.fitserver.domain.alarm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmCase {
  FAILED_MATCHING(
      AlarmType.MATCHING,
      "새로운 매칭을 시작하세요.",
      "충분한 팀원이 모이지 않아 매칭이 성사되지 않았습니다."),
  STARTED_PROJECT(AlarmType.PROJECT,
      "프로젝트가 시작되었어요.",
      "팀원을 확인해보세요!"),
  START_PROJECT(AlarmType.MATCHING,
      "프로젝트를 시작해주세요.",
      "대기방의 모든 인원이 참여상태입니다."),
  NEW_MATCHING_ROOM(AlarmType.MATCHING,
      "대기방이 생성되었어요.",
      "랜덤매칭이 성사되었습니다. 대기방을 확인해보세요!"),
  PROGRESS_MATCHING(AlarmType.MATCHING,
      "매칭이 진행 중이에요.",
      null),
  FORCE_OUT(AlarmType.MATCHING,
      "대기방에서 강퇴되었어요.",
      "새로운 랜덤매칭을 이용해주세요!"),
  NEW_MESSAGE_MATCHING(AlarmType.MATCHING,
      "새로운 메시지가 있어요.",
      "확인해보세요!"),
  NEW_MESSAGE_PROJECT(AlarmType.PROJECT,
      "새로운 메시지가 있어요.",
      "확인해보세요!"),
  REPORT(AlarmType.REPORT,
      "신고가 접수되었어요.",
      "관리자 확인 후, 상대의 서비스 이용이 제한됩니다."),
  ;

  private final AlarmType alarmType;
  private final String title;
  private final String description;
}
