package org.crops.fitserver.domain.project.dto.request;

import org.crops.fitserver.domain.project.constant.ReportType;

public record ReportProjectMemberRequest(
    Long targetUserId, ReportType reportType, String description
) {

}
