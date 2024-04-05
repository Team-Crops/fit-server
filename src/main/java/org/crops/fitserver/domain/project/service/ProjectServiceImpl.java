package org.crops.fitserver.domain.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.project.constant.ProjectStatus;
import org.crops.fitserver.domain.project.dto.ProjectDto;
import org.crops.fitserver.domain.project.dto.ProjectMemberDto;
import org.crops.fitserver.domain.project.dto.request.UpdateProjectRequest;
import org.crops.fitserver.domain.project.dto.response.GetProjectListResponse;
import org.crops.fitserver.domain.project.dto.response.UpdateProjectResponse;
import org.crops.fitserver.domain.project.entity.Project;
import org.crops.fitserver.domain.project.entity.ProjectMember;
import org.crops.fitserver.domain.project.repository.ProjectMemberRepository;
import org.crops.fitserver.domain.project.repository.ProjectRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;

  @Override
  public GetProjectListResponse getProjectList(Long userId) {
    var projectList = projectRepository.findAllByUserId(userId);

    var projectDtoList = projectList.stream()
        .map(p -> this.toProjectDto(p, userId))
        .sorted(this::getSortedComparisonValue)
        .toList();

    return new GetProjectListResponse(projectDtoList);
  }

  @Override
  public UpdateProjectResponse updateProject(Long userId, Long projectId,
      UpdateProjectRequest request) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_PROJECT_EXCEPTION));

    var projectMember = project.getProjectMemberList().stream()
        .filter(pm -> pm.getUser().getId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));

    projectMember.updateProjectName(request.projectName());
    projectMember.updateStatus(request.projectStatus());

    projectMemberRepository.save(projectMember);

    return new UpdateProjectResponse(toProjectDto(project, userId));
  }

  private ProjectDto toProjectDto(Project project, Long userId) {
    var owner = this.findUserInProject(userId, project);
    return ProjectDto.builder()
        .projectId(project.getId())
        .chatRoomId(project.getChatRoomId())
        .projectMemberList(
            project.getProjectMemberList().stream()
                .map(ProjectMemberDto::from)
                .toList())
        .projectStatus(owner.getStatus())
        .projectName(owner.getProjectName())
        .createdAt(owner.getCreatedAt())
        .completedAt(owner.getCompletedAt())
        .build();
  }

  private ProjectMember findUserInProject(Long userId, Project project) {
    return project.getProjectMemberList().stream()
        .filter(pm -> pm.getUser().getId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }

  private int getSortedComparisonValue(ProjectDto p1, ProjectDto p2) {
    if (p1.projectStatus().equals(p2.projectStatus())) {
      if (p1.projectStatus().equals(ProjectStatus.PROJECT_IN_PROGRESS)) {
        return p2.createdAt().compareTo(p1.createdAt());
      } else {
        return p2.completedAt().compareTo(p1.completedAt());
      }
    } else {
      return p1.projectStatus().getStep() - p2.projectStatus().getStep();
    }
  }
}
