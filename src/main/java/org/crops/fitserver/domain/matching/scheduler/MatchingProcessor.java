package org.crops.fitserver.domain.matching.scheduler;


import static java.util.stream.Collectors.groupingBy;
import static org.crops.fitserver.domain.matching.VO.ComparableMatchingParameter.calculateSimilarity;
import static org.crops.fitserver.domain.matching.VO.ComparableMatchingParameter.isSimilar;
import static org.crops.fitserver.domain.matching.constant.MatchingConstants.MINIMUM_REQUIRED_POSITIONS;

import io.jsonwebtoken.lang.Collections;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.domain.ChatRoom;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.matching.VO.ComparableMatchingParameter;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingProcessor {

  private final MatchingRepository matchingRepository;
  private final MatchingRoomRepository matchingRoomRepository;
  private final ChatRoomService chatRoomService;

  private static final int BATCH_SIZE = 100;

  @Transactional
  public void match() {

    var matchingRoomList = matchingRoomRepository.findMatchingRoomNotComplete(LocalDateTime.now().minusDays(5));
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


  @Transactional
  public void insertToNotEnoughRoom(List<MatchingRoom> matchingRoomList,
      Map<PositionType, List<Matching>> matchingMap) {
    var notEnoughRoomList = matchingRoomList.stream()
        .filter(MatchingRoom::isNotEnough).toList();

    //포지션별로 매칭이 부족한 룸을 나눔
    var notEnoughRoomMap = new HashMap<PositionType, List<MatchingRoom>>();
    notEnoughRoomList.forEach(matchingRoom -> {
      var PositionTypeSet = matchingMap.keySet();
      PositionTypeSet.forEach(positionType -> {
        if (matchingRoom.isEnough(positionType)) {
          return;
        }
        notEnoughRoomMap.computeIfAbsent(positionType, key -> new ArrayList<>())
            .add(matchingRoom);
      });
    });

    var updateRoomList = new HashSet<MatchingRoom>();

    //부족한 매칭을 채워넣음
    matchingMap.forEach((key, value) -> {
      if(Collections.isEmpty(notEnoughRoomMap.get(key))){
        return;
      }
      var targetRoomList = notEnoughRoomMap.get(key).stream()
          .filter(matchingRoom -> matchingRoom.isNotEnough(key))
          .toList();

      value.forEach(matching -> {
        var bestRoom = findBestRoom(matching, targetRoomList);
        bestRoom.ifPresent(matchingRoom -> {
          matchingRoom.addMatching(matching);
          chatRoomService.chatRoomJoin(matchingRoom.getChatRoomId(), matching.getUser());
          updateRoomList.add(matchingRoom);
        });
      });
    });

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
  }

  @Transactional
  public void createNewRoom(
      Map<PositionType, List<Matching>> matchingMap) {
    if (matchingMap.size() < MINIMUM_REQUIRED_POSITIONS.size()) {
      return;
    }
    while (matchingMap.values().stream().mapToInt(List::size).min().orElse(0) > 0) {
      var matchingList = new ArrayList<Matching>();
      matchingMap.forEach((key, value) -> {
        if(Collections.isEmpty(value)) {
          return;
        }
        var bestMatching = findBestMatching(matchingList, value);
        bestMatching.ifPresent((matching -> {
          matchingList.add(matching);
          value.remove(matching);
        }));
      });
      if (matchingList.size() < MINIMUM_REQUIRED_POSITIONS.size()) {
        continue;
      }
      ChatRoom chatRoom = chatRoomService.createChatRoom();
      matchingList.forEach(matching -> {
        User user = matching.getUser();
        chatRoomService.chatRoomJoin(chatRoom.getId(), user);
      });
      var newRoom = MatchingRoom.create(matchingList, chatRoomService.createChatRoom().getId());
      matchingRoomRepository.save(newRoom);
    }
  }

  @Transactional
  public void joinRoom(List<MatchingRoom> matchingRoomList,
      Map<PositionType, List<Matching>> matchingMap) {
    //frontend, backend, designer, planner순으로 룸에 들어가기
    joinRoom(PositionType.FRONTEND, matchingMap, matchingRoomList);
    joinRoom(PositionType.BACKEND, matchingMap, matchingRoomList);
    joinRoom(PositionType.DESIGNER, matchingMap, matchingRoomList);
    joinRoom(PositionType.PLANNER, matchingMap, matchingRoomList);
  }

  private void joinRoom(PositionType positionType, Map<PositionType, List<Matching>> matchingMap,
      List<MatchingRoom> matchingRoomList) {
    var matchingList = matchingMap.get(positionType);
    var roomList = new ArrayList<>(matchingRoomList.stream()
        .filter(matchingRoom -> matchingRoom.canInsertPosition(positionType))
        .toList());
    if (Collections.isEmpty(matchingList) || Collections.isEmpty(roomList)) {
      return;
    }

    var removeList = new ArrayList<Matching>();

    matchingList.forEach(matching -> {
      this.findBestRoom(matching, roomList)
          .ifPresent(matchingRoom -> {
            matchingRoom.addMatching(matching);
            matchingRoomRepository.save(matchingRoom);
            chatRoomService.chatRoomJoin(matchingRoom.getChatRoomId(), matching.getUser());
            removeList.add(matching);
          });
    });

    matchingList.removeAll(removeList);
  }

  /**
   * TODO: 최적의 매칭 방을 찾아서 반환
   */
  private Optional<MatchingRoom> findBestRoom(Matching matching, List<MatchingRoom> roomList) {
    var comparableParameterFromMatching = ComparableMatchingParameter.from(matching);

    //roomList에서 matching이 들어갈 수 있는 방을 찾아서 반환
    //matching이 들어갈 수 있는 방은 MatchingRoom.canJoinRoom()을 사용
    //roomList에서 avgActivityHour, avgProjectCount와 가장 가까운 매칭을 찾아서 반환
    //유사도 알고리즘 적용. ActivityHour의 가중치는 10, ProjectCount의 가중치는 5
    var insertableRoomList = roomList.stream()
        .filter(matchingRoom -> matchingRoom.canJoinRoom(matching))
        .filter(matchingRoom -> isSimilar(comparableParameterFromMatching,
            ComparableMatchingParameter.from(matchingRoom.getMatchingList())))
        .findFirst();

    return insertableRoomList.stream()
        .min(Comparator.comparingDouble((matchingRoom) -> calculateSimilarity(
            comparableParameterFromMatching,
            ComparableMatchingParameter.from(matchingRoom.getMatchingList())))
        );
  }

  /**
   * matchingList: 매칭방을 만들기 전 매칭 리스트(임시 매칭방)
   * targetMatchingList: 매칭리스트
   */
  private Optional<Matching> findBestMatching(List<Matching> matchingList,
      List<Matching> targetMatchingList) {
    if(Collections.isEmpty(matchingList)) {
      return Optional.of(targetMatchingList.get(0));
    }
    var region = matchingList.get(0).getUser().getUserInfo().getRegion();

    //targetMatchingList에서 region이 같은 매칭을 찾아서 반환
    //targetMatchingList에서 avgActivityHour, avgProjectCount와 가장 가까운 매칭을 찾아서 반환
    //유사도 알고리즘 적용. ActivityHour의 가중치는 10, ProjectCount의 가중치는 5
    var insertableMatchingList = targetMatchingList.stream()
        .filter((matching) -> matching.getUser().getUserInfo().getRegion().equals(region))
        .filter(matching -> isSimilar(ComparableMatchingParameter.from(matching),
            ComparableMatchingParameter.from(matchingList)))
        .toList();

    return insertableMatchingList.stream()
        .min(Comparator.comparingDouble((matching) -> calculateSimilarity(
            ComparableMatchingParameter.from(matching),
            ComparableMatchingParameter.from(matchingList)))
        );
  }
}
