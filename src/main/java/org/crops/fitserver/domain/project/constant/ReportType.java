package org.crops.fitserver.domain.project.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
  ESCAPE("탈주"),
  DIVE("잠수"),
  ETC("기타")
  ;

  private final String name;
}
