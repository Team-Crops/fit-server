package org.crops.fitserver.domain.project.dto.request;

import org.crops.fitserver.domain.project.constant.ProjectStatus;

public record UpdateProjectRequest(
    String projectName,
    ProjectStatus projectStatus
) {

}
