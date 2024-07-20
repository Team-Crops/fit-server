package org.crops.fitserver.global.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.crops.fitserver.domain.project.constant.ReportType;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@ToString
public class Report implements Message {

  Long targetUserId;
  ReportType reportType;
}


