package org.crops.fitserver.domain.matching.scheduler;


import static java.util.stream.Collectors.groupingBy;

import io.jsonwebtoken.lang.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.springframework.transaction.annotation.Transactional;


/**
 * TODO: 싱글턴으로 전환
 */
@Slf4j
public class MatchingProcessor {

  private final MatchingRepository matchingRepository;
  private final MatchingRoomRepository matchingRoomRepository;
  private final ChatRoomService chatRoomService;
  private List<MatchingRoom> matchingRoomList;

  private Map<PositionType, List<Matching>> matchingMap;

  public MatchingProcessor(MatchingRepository matchingRepository,
      MatchingRoomRepository matchingRoomRepository, ChatRoomService chatRoomService) {
    this.matchingRepository = matchingRepository;
    this.matchingRoomRepository = matchingRoomRepository;
    this.chatRoomService = chatRoomService;

    init();
  }

  private void init() {
    List<Matching> matchingList = matchingRepository.findMatchingWithoutRoom();

    matchingMap = matchingList.stream()
        .collect(groupingBy(matching -> matching.getPosition().getType()));

    matchingRoomList = matchingRoomRepository.findMatchingRoomNotComplete();

    matchingMap.forEach((key, value) -> {
      value.sort(Comparator.comparing(Matching::getExpiredAt));
    });
  }

  @Transactional
  public void insertToNotEnoughRoom() {
    log.info("insertToNotEnoughRoom start");
    var notEnoughRoomList = matchingRoomList.stream()
        .filter(MatchingRoom::isNotEnough).toList();
    notEnoughRoomList
        .forEach(matchingRoom -> matchingMap.forEach((key, value) -> {
          if (matchingRoom.isNotEnough(key)) {
            matchingRoom.addMatching(value.remove(0));
          }
        }));

    matchingRoomRepository.saveAll(notEnoughRoomList);

    log.info("insertToNotEnoughRoom end. size: {}",
        notEnoughRoomList.size());
  }

  @Transactional
  public void createNewRoom() {
    if (matchingMap.size() < 4) {
      return;
    }
    log.info("createNewRoom start");
    var size = matchingMap.values().stream().mapToInt(List::size).min().orElse(0);

    for (int i = 0; i < size; i++) {
      var matchingList = new ArrayList<Matching>();
      matchingMap.forEach((key, value) -> {
        matchingList.add(value.remove(0));
      });
      var newRoom = MatchingRoom.create(matchingList, chatRoomService.createChatRoom().getId());
      matchingRoomRepository.save(newRoom);
    }

    log.info("createNewRoom end. size: {}", size);
  }

  @Transactional
  public void joinRoom() {
    log.info("joinRoom start");

    int count = 0;

    //frontend, backend, designer, planner순으로 룸에 들어가기
    count += joinRoom(PositionType.FRONTEND);
    count += joinRoom(PositionType.BACKEND);
    count += joinRoom(PositionType.DESIGNER);
    count += joinRoom(PositionType.PLANNER);

    log.info("joinRoom end. count: {}", count);
  }

  private int joinRoom(PositionType positionType) {
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
   * 최소한의 필터
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