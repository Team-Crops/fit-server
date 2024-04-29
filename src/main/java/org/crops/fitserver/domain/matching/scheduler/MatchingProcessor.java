package org.crops.fitserver.domain.matching.scheduler;


import static java.util.stream.Collectors.groupingBy;

import io.jsonwebtoken.lang.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.springframework.stereotype.Service;


/**
 * TODO: 싱글턴으로 전환
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingProcessor {

  private final MatchingRepository matchingRepository;
  private final MatchingRoomRepository matchingRoomRepository;
  private final ChatRoomService chatRoomService;

  private static final int BATCH_SIZE = 100;

  public void match() {

    var matchingRoomList = matchingRoomRepository.findMatchingRoomNotComplete();
    var matchingList = matchingRepository.findMatchingWithoutRoom();

    var matchingMap = getMatchingMap(matchingList);

    insertToNotEnoughRoom(matchingRoomList, matchingMap);

    createNewRoom(matchingMap);

    joinRoom(matchingRoomList, matchingMap);
  }

  private Map<PositionType, List<Matching>> getMatchingMap(List<Matching> matchingList) {
    var matchingMap = matchingList.stream()
        .collect(groupingBy(matching -> matching.getPosition().getType()));

    matchingMap.forEach((key, value) -> value.sort(Comparator.comparing(Matching::getExpiredAt)));

    return matchingMap;
  }


  public void insertToNotEnoughRoom(List<MatchingRoom> matchingRoomList,
      Map<PositionType, List<Matching>> matchingMap) {
    var startTime = System.currentTimeMillis();
    var notEnoughRoomList = matchingRoomList.stream()
        .filter(MatchingRoom::isNotEnough).toList();

    var updateRoomList = new HashSet<MatchingRoom>();

    notEnoughRoomList
        .forEach(matchingRoom -> matchingMap.forEach((key, value) -> {
          if (matchingRoom.isNotEnough(key)) {
            matchingRoom.addMatching(value.remove(0));
            updateRoomList.add(matchingRoom);
          }
        }));

    //100개씩 업데이트. 중간에 하나 에러나도 다른 것들은 업데이트 되도록
    for (int i = 0; i < updateRoomList.size(); i += BATCH_SIZE) {
      var subList = new ArrayList<>(updateRoomList)
          .subList(i, Math.min(i + BATCH_SIZE, updateRoomList.size()));
      try {
        matchingRoomRepository.saveAllAndFlush(subList);
      } catch (Exception e) {
        log.error("insertToNotEnoughRoom error", e);
      }
    }

    if (!notEnoughRoomList.isEmpty()) {
      var endTime = System.currentTimeMillis();
      log.info(
          "process insertToNotEnoughRoom. size: {}, start time: {}, end time: {}, total time: {}",
          notEnoughRoomList.size(), startTime, endTime, endTime - startTime);
    }
  }


  public void createNewRoom(
      Map<PositionType, List<Matching>> matchingMap) {
    if (matchingMap.size() < 4) {
      return;
    }
    var startTime = System.currentTimeMillis();

    var size = matchingMap.values().stream().mapToInt(List::size).min().orElse(0);

    for (int i = 0; i < size; i++) {
      var matchingList = new ArrayList<Matching>();
      matchingMap.forEach((key, value) -> {
        matchingList.add(value.remove(0));
      });
      var newRoom = MatchingRoom.create(matchingList, chatRoomService.createChatRoom().getId());
      matchingRoomRepository.save(newRoom);
    }

    if (size > 0) {
      var endTime = System.currentTimeMillis();
      log.info("process createNewRoom. size: {}, start time: {}, end time: {}, total time: {}",
          size, startTime, endTime, endTime - startTime);
    }

  }


  public void joinRoom(List<MatchingRoom> matchingRoomList,
      Map<PositionType, List<Matching>> matchingMap) {
    var startTime = System.currentTimeMillis();

    int count = 0;

    //frontend, backend, designer, planner순으로 룸에 들어가기
    count += joinRoom(PositionType.FRONTEND, matchingMap, matchingRoomList);
    count += joinRoom(PositionType.BACKEND, matchingMap, matchingRoomList);
    count += joinRoom(PositionType.DESIGNER, matchingMap, matchingRoomList);
    count += joinRoom(PositionType.PLANNER, matchingMap, matchingRoomList);

    if (count > 0) {
      var endTime = System.currentTimeMillis();
      log.info("process joinRoom. size: {}, start time: {}, end time: {}, total time: {}",
          count, startTime, endTime, endTime - startTime);
    }
  }

  private int joinRoom(PositionType positionType, Map<PositionType, List<Matching>> matchingMap,
      List<MatchingRoom> matchingRoomList) {
    var matchingList = matchingMap.get(positionType);
    var roomList = new ArrayList<>(matchingRoomList.stream()
        .filter(matchingRoom -> matchingRoom.canInsertPosition(positionType))
        .toList());
    if (Collections.isEmpty(matchingList) || Collections.isEmpty(roomList)) {
      return 0;
    }

    var removeList = new ArrayList<Matching>();

    matchingList.forEach(matching -> {
      var enableRoomList = filterEnableRoomList(matching, roomList, positionType);

      this.findBestRoom(enableRoomList)
          .ifPresent(matchingRoom -> {
            matchingRoom.addMatching(matching);
            matchingRoomRepository.save(matchingRoom);
            removeList.add(matching);
          });
    });

    matchingList.removeAll(removeList);

    return matchingList.size();
  }

  /**
   * 필수 필터 조건에 맞는 방만 필터링
   */
  private List<MatchingRoom> filterEnableRoomList(Matching matching, List<MatchingRoom> roomList,
      PositionType positionType) {
    return roomList.stream()
        .filter(matchingRoom -> matchingRoom.canJoinRoom(matching, positionType))
        .toList();
  }

  /**
   * TODO: 최적의 매칭 방을 찾아서 반환
   */
  private Optional<MatchingRoom> findBestRoom(List<MatchingRoom> roomList) {
    return roomList.stream()
        .findFirst();
  }
}
