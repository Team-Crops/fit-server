package org.crops.fitserver.domain.user.service;

import org.crops.fitserver.domain.project.constant.ReportType;

public interface UserBlockService {

    boolean canBlockUser(Long userId, ReportType reportType);

    void blockUser(Long userId);
}
