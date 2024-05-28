package org.crops.fitserver.domain.mail.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.project.constant.ReportType;
import org.crops.fitserver.domain.user.domain.User;

@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportMailRequiredInfo implements MailRequiredInfo {

  private final String reporterUsername;
  private final String reporterNickname;
  private final Long reporterId;
  private final String reporterEmail;
  private final String reportedUsername;
  private final String reportedNickname;
  private final Long reportedId;
  private final String reportedEmail;
  private final ReportType reportType;
  private final String reportContent;

  public static ReportMailRequiredInfo of(User reporter, User reported, ReportType reportType, String reportContent) {
    return ReportMailRequiredInfo.builder()
      .reporterUsername(reporter.getUsername())
      .reporterNickname(reporter.getNickname())
      .reporterId(reporter.getId())
      .reporterEmail(reporter.getEmail())
      .reportedUsername(reported.getUsername())
      .reportedNickname(reported.getNickname())
      .reportedId(reported.getId())
      .reportedEmail(reported.getEmail())
      .reportType(reportType)
      .reportContent(reportContent)
      .build();
  }



  @Override
  public String replace(String content) {
    return content
      .replace("${reporter.username}", reporterUsername)
      .replace("${reporter.nickname}", reporterNickname)
      .replace("${reporter.id}", String.valueOf(reporterId))
      .replace("${reporter.email}", reporterEmail)
      .replace("${reported.username}", reportedUsername)
      .replace("${reported.nickname}", reportedNickname)
      .replace("${reported.dd}", String.valueOf(reportedId))
      .replace("${reported.email}", reportedEmail)
      .replace("${reportType}", reportType.getName())
      .replace("${reportContent}", reportContent);
  }
}
