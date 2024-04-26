package org.crops.fitserver.domain.user.service;

import static org.crops.fitserver.domain.user.constant.BlockConstants.REPORT_MIN_COUNT;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.project.domain.ProjectReportHistory;
import org.crops.fitserver.domain.project.repository.ProjectReportHistoryRepository;
import org.crops.fitserver.domain.user.domain.UserBlock;
import org.crops.fitserver.domain.user.repository.UserBlockRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBlockServiceImpl implements UserBlockService {

  private final UserRepository userRepository;
  private final ProjectReportHistoryRepository projectReportHistoryRepository;
  private final UserBlockRepository userBlockRepository;

  public boolean canBlockUser(Long userId) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
    var reportCount = projectReportHistoryRepository.countByTargetUserId(user.getId());
    return reportCount >= REPORT_MIN_COUNT;
  }

  @Transactional
  public void blockUser(Long userId) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
    Optional<UserBlock> block = userBlockRepository.findActiveBlock(user);

    if (block.isEmpty()) {
      userBlockRepository.save(UserBlock.create(user));
      return;
    }

    var reportHistoryList = projectReportHistoryRepository.findAllByTargetUserId(
        user.getId());

    var lastReportProjectId = getLastReportProjectId(reportHistoryList);

    if (reportHistoryList.stream()
        .map(ProjectReportHistory::getProjectId)
        .noneMatch(lastReportProjectId::equals)) {
      block.get().extendBlock();
    }

  }

  private Long getLastReportProjectId(List<ProjectReportHistory> reportHistoryList) {
    return reportHistoryList.stream()
        .max(Comparator.comparing(ProjectReportHistory::getCreatedAt))
        .map(ProjectReportHistory::getProjectId)
        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
