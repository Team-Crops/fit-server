package org.crops.fitserver.domain.matching.service.impl;

import static org.crops.fitserver.domain.project.util.ProjectConverter.convertMatchingRoomToProject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.alarm.domain.AlarmCase;
import org.crops.fitserver.domain.alarm.service.AlarmService;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.dto.MatchingDto;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.domain.project.repository.ProjectRepository;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.repository.UserBlockRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

  private final MatchingRepository matchingRepository;
  private final MatchingRoomRepository matchingRoomRepository;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final ChatRoomService chatRoomService;
  private final AlarmService alarmService;
  private final UserBlockRepository userBlockRepository;

  @Override
  @Transactional
  public MatchingDto createMatching(Long userId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    if (userBlockRepository.findActiveBlock(user).isPresent()) {
      throw new BusinessException(ErrorCode.BLOCKED_USER_EXCEPTION);
    }

    if (getActiveMatching(user).isPresent()) {
      throw new BusinessException(ErrorCode.ALREADY_EXIST_MATCHING_EXCEPTION);
    }
    var matching = matchingRepository.save(Matching.create(user));
    alarmService.sendAlarm(user, AlarmCase.PROGRESS_MATCHING);
    return MatchingDto.from(matching);
  }

  @Override
  @Transactional(readOnly = true)
  public MatchingDto getMatching(Long userId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    return MatchingDto.from(matching);
  }

  @Override
  @Transactional(readOnly = true)
  public GetMatchingRoomResponse getMatchingRoom(Long userId, Long roomId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    if (!Objects.equals(matching.getMatchingRoom().getId(), roomId)) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION);
    }
    var matchingRoom = matchingRoomRepository.findWithMatchingMembersById(roomId)
        .orElseThrow(() -> new BusinessException(
            ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION));

    return GetMatchingRoomResponse.from(matchingRoom);
  }

  @Override
  @Transactional
  public void ready(Long userId, Long roomId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));
    var matchingRoom = matching.getMatchingRoom();

    if (!Objects.equals(matchingRoom.getId(), roomId)) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION);
    }
    if (Objects.equals(matchingRoom.getHostUserId(), userId)) {
      throw new BusinessException(ErrorCode.NOT_ENABLE_READY_EXCEPTION);
    }

    matchingRoom.ready(matching);
    matchingRepository.save(matching);
  }

  @Override
  @Transactional
  public void cancelReady(Long userId, Long roomId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));
    var matchingRoom = matching.getMatchingRoom();

    if (!Objects.equals(matchingRoom.getId(), roomId)) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION);
    }
    if (Objects.equals(matchingRoom.getHostUserId(), userId)) {
      throw new BusinessException(ErrorCode.NOT_ENABLE_READY_EXCEPTION);
    }

    matchingRoom.cancelReady(matching);
    matchingRepository.save(matching);
  }

  @Override
  @Transactional
  public void complete(Long userId, Long roomId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));
    var matchingRoom = matching.getMatchingRoom();

    if (!Objects.equals(matchingRoom.getId(), roomId)) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION);
    }
    if (!Objects.equals(matchingRoom.getHostUserId(), userId)) {
      throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION);
    }

    matchingRoom.complete();
    matchingRoom.getMatchingList().forEach(m ->
        alarmService.sendAlarm(m.getUser(), AlarmCase.STARTED_PROJECT));
    matchingRoomRepository.save(matchingRoom);
    createProject(matchingRoom);
  }

  private void createProject(MatchingRoom matchingRoom) {
    var project = convertMatchingRoomToProject(matchingRoom);
    projectRepository.save(project);
  }

  @Override
  @Transactional
  public void exit(Long userId, Long roomId) {

    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));
    var matchingRoom = matching.getMatchingRoom();
    if (!Objects.equals(matchingRoom.getId(), roomId)) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION);
    }

    chatRoomService.chatRoomLeave(matchingRoom.getChatRoomId(), user);
    matchingRoom.exit(matching);
  }

  @Override
  @Transactional
  public void cancel(Long userId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    matching.cancel();
  }

  @Override
  @Transactional
  public List<Matching> expireMatchingAll() {
    var matchingList = matchingRepository.findExpireMatching();
    matchingList.forEach(Matching::expire);
    matchingRepository.saveAll(matchingList);
    return matchingList;
  }

  @Override
  @Transactional
  public void forceOut(Long userId, Long roomId, Long forceOutUserId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));
    var matchingRoom = matching.getMatchingRoom();

    if (!Objects.equals(matchingRoom.getId(), roomId)) {
      throw new BusinessException(ErrorCode.NOT_EXIST_MATCHING_EXCEPTION);
    }
    matchingRoom.forceOut(userId, forceOutUserId);
    matchingRoomRepository.save(matchingRoom);

    chatRoomService.chatRoomForceOut(matchingRoom.getChatRoomId(), user);
    alarmService.sendAlarm(user, AlarmCase.FORCE_OUT);
  }

  private Optional<Matching> getActiveMatching(User user) {
    return matchingRepository.findMatchingByUserAndStatusIn(user,
        MatchingStatus.getActiveStatusList());
  }
}
