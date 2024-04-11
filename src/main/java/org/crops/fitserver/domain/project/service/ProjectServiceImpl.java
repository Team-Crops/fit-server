package org.crops.fitserver.domain.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.project.constant.ProjectStatus;
import org.crops.fitserver.domain.project.dto.ProjectDto;
import org.crops.fitserver.domain.project.dto.request.UpdateProjectRequest;
import org.crops.fitserver.domain.project.dto.response.GetProjectListResponse;
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
        .map(project -> ProjectDto.of(project, userId))
        .sorted(this::getSortedComparisonValue)
        .toList();

    return GetProjectListResponse.from(projectDtoList);
  }

  @Override
  public ProjectDto updateProject(Long userId, Long projectId,
      UpdateProjectRequest request) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_PROJECT_EXCEPTION));

    var projectMember = project.getProjectMemberList().stream()
        .filter(pm -> pm.getUser().getId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_PROJECT_EXCEPTION));

    projectMember.updateProjectName(request.projectName());
    projectMember.updateStatus(request.projectStatus());

    return ProjectDto.of(project, userId);
  }

  private int getSortedComparisonValue(ProjectDto p1, ProjectDto p2) {
    if (!p1.projectStatus().equals(p2.projectStatus())) {
      return p1.projectStatus().getStep() - p2.projectStatus().getStep();
    }

    return p1.projectStatus().equals(ProjectStatus.PROJECT_COMPLETE) ?
        p2.completedAt().compareTo(p1.completedAt()) : p2.createdAt().compareTo(p1.createdAt());
  }
}
