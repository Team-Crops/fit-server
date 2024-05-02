package org.crops.fitserver.domain.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.project.dto.ProjectDto;
import org.crops.fitserver.domain.project.dto.request.ReportProjectMemberRequest;
import org.crops.fitserver.domain.project.dto.request.UpdateProjectRequest;
import org.crops.fitserver.domain.project.dto.response.GetProjectListResponse;
import org.crops.fitserver.domain.project.service.ProjectService;
import org.crops.fitserver.global.annotation.CurrentUserId;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping
  public ResponseEntity<GetProjectListResponse> getProjectList(@CurrentUserId Long userId) {
    return ResponseEntity.ok(projectService.getProjectList(userId));
  }

  @PatchMapping("/{projectId}")
  public ResponseEntity<ProjectDto> updateProject(@CurrentUserId Long userId,
      @PathVariable Long projectId, @RequestBody UpdateProjectRequest request) {
    return ResponseEntity.ok(projectService.updateProject(userId, projectId, request));
  }

  @PostMapping("/{projectId}/report")
  public ResponseEntity<Void> reportProjectMember(@CurrentUserId Long userId,
      @PathVariable Long projectId, @RequestBody ReportProjectMemberRequest request) {
    projectService.reportProjectMember(userId, projectId, request);

    return ResponseEntity.noContent().build();
  }
}
