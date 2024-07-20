package org.crops.fitserver.domain.project.service;

import org.crops.fitserver.domain.project.dto.ProjectDto;
import org.crops.fitserver.domain.project.dto.request.ReportProjectMemberRequest;
import org.crops.fitserver.domain.project.dto.request.UpdateProjectRequest;
import org.crops.fitserver.domain.project.dto.response.GetProjectListResponse;
import org.crops.fitserver.domain.project.dto.response.UpdateProjectResponse;

public interface ProjectService {

  GetProjectListResponse getProjectList(Long userId);

  ProjectDto updateProject(Long userId, Long projectId, UpdateProjectRequest request);

  void reportProjectMember(Long userId, Long projectId, ReportProjectMemberRequest request);
}
