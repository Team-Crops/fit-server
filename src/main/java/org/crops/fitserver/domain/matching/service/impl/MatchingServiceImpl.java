package org.crops.fitserver.domain.matching.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.dto.response.CreateMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.domain.user.domain.User;
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

  @Override
  @Transactional
  public CreateMatchingResponse createMatching(Long userId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    //이미 매칭이 존재한다면 에러 발생.
    if (getActiveMatching(user).isPresent()) {
      throw new BusinessException(ErrorCode.ALREADY_EXIST_MATCHING_EXCEPTION);
    }

    var matching = matchingRepository.save(Matching.create(user, user.getUserInfo().getPosition()));
    return CreateMatchingResponse.from(matching);
  }

  @Override
  @Transactional(readOnly = true)
  public GetMatchingResponse getMatching(Long userId) {
    var user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    var matching = getActiveMatching(user).orElseThrow(() -> new BusinessException(
        ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));

    return GetMatchingResponse.from(matching);
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
    var matchingRoom = matchingRoomRepository.findById(roomId)
        .orElseThrow(() -> new BusinessException(
            ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION));

    return GetMatchingRoomResponse.from(matchingRoom);
  }

  @Override
  @Transactional
  public void readyMatching(Long userId, Long roomId) {
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

    matching.ready();
    matchingRepository.save(matching);
  }

  @Override
  @Transactional
  public void completeMatching(Long userId, Long roomId) {
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
    matchingRoomRepository.save(matchingRoom);
    //TODO: 내 프로젝트 생성 로직 추가
  }

  @Override
  @Transactional
  public void cancelMatching(Long userId) {
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
    //TODO: 채팅방에 강제퇴장 메시지 전송
    //TODO: 강제퇴장당한 유저에게 강제퇴장 알림 전송
  }

  private Optional<Matching> getActiveMatching(User user) {
    return matchingRepository.findActiveMatchingByUser(user,
        List.of(MatchingStatus.WAITING, MatchingStatus.MATCHED));
  }
}
