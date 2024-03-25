package org.crops.fitserver.domain.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class Project extends BaseTimeEntity {

  @OneToMany(mappedBy = "project")
  private final List<ProjectMember> projectMemberList = new ArrayList<>();
  @Id
  @Column(name = "project_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "chat_room_id", nullable = false)
  private Long chatRoomId;

  public static Project create(Long chatRoomId) {
    return Project.builder()
        .chatRoomId(chatRoomId)
        .build();
  }

  public void addMember(ProjectMember projectMember) {
    if (projectMemberList.stream()
        .anyMatch(member -> member.getUser().equals(projectMember.getUser()))) {
      return;
    }
    projectMemberList.add(projectMember);
  }

  public void removeMember(ProjectMember projectMember) {
    this.projectMemberList.removeIf(member -> member.getUser().equals(projectMember.getUser()));
  }
}
