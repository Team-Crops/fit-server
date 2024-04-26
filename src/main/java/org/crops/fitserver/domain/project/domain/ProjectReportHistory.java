package org.crops.fitserver.domain.project.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.project.constant.ReportType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
public class ProjectReportHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long reporterUserId;

  @Column(nullable = false)
  private Long targetUserId;

  @Column(nullable = false)
  private Long projectId;

  @Column(nullable = false)
  private ReportType reportType;

  private String description;


  public static ProjectReportHistory create(Long reporterUserId, Long targetUserId, Long projectId, ReportType reportType, String description) {
    return ProjectReportHistory.builder()
        .reporterUserId(reporterUserId)
        .targetUserId(targetUserId)
        .projectId(projectId)
        .reportType(reportType)
        .description(description)
        .build();
  }

}
