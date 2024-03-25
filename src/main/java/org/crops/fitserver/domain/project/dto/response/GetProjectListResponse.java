package org.crops.fitserver.domain.project.dto.response;

import java.util.List;
import org.crops.fitserver.domain.project.dto.ProjectDto;

public record GetProjectListResponse(
    List<ProjectDto> projectList
) {

  public static GetProjectListResponse create(List<ProjectDto> projectList) {
    return new GetProjectListResponse(projectList.stream().toList());
  }

}
