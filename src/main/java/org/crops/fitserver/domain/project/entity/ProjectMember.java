package org.crops.fitserver.domain.project.entity;

import static org.crops.fitserver.domain.project.constant.Constant.PROJECT_DEFAULT_NAME;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.project.constant.ProjectStatus;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
public class ProjectMember extends BaseTimeEntity {

  @Id
  @Column(name = "project_member_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "position_id", nullable = false)
  private Position position;

  @Column(name = "completed_at", nullable = true)
  private LocalDateTime completedAt;

  @Column(name = "status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ProjectStatus status;

  @Column(name = "project_name", nullable = false)
  private String projectName;

  public static ProjectMember create(User user, Project project, Position position) {
    var newProjectMember = ProjectMember.builder()
        .user(user)
        .project(project)
        .position(position)
        .completedAt(null)
        .status(ProjectStatus.PROJECT_IN_PROGRESS)
        .projectName(PROJECT_DEFAULT_NAME)
        .build();

    project.addMember(newProjectMember);

    return newProjectMember;
  }

  public void updateProjectName(String projectName) {
    if (StringUtils.isNotBlank(projectName)) {
      this.projectName = projectName;
    }
  }

  public void updateStatus(ProjectStatus status) {
    if (status != null) {
      this.status = status;
      this.completedAt = status.equals(ProjectStatus.PROJECT_COMPLETE) ? LocalDateTime.now() : null;
    }
  }
}
