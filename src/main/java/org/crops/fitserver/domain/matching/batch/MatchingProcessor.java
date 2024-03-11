package org.crops.fitserver.domain.matching.batch;


import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.springframework.transaction.annotation.Transactional;

public class MatchingProcessor {

  private final MatchingRepository matchingRepository;
  private final MatchingRoomRepository matchingRoomRepository;
  private final ChatRoomService chatRoomService;
  private List<Matching> matchingList;
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
    matchingList = matchingRepository.findMatchingWithoutRoom();
    matchingRoomList = matchingRoomRepository.findMatchingRoomNotComplete();

    matchingMap = matchingList.stream()
        .collect(groupingBy(matching -> matching.getPosition().getType()));

    //매칭 만료 시간 순으로 정렬
    matchingMap.forEach((key, value) -> {
      value.sort(Comparator.comparing(Matching::getExpiredAt));
    });
  }

  //최소 인원이 채워지지 않은 룸에 매칭을 시도
  @Transactional
  public void insertToNotEnoughRoom() {
    var notEnoughRoomList = matchingRoomList.stream()
        .filter(MatchingRoom::isNotEnough).toList();
    notEnoughRoomList
        .forEach(matchingRoom -> matchingMap.forEach((key, value) -> {
          if (matchingRoom.isNotEnough(key)) {
            matchingRoom.addMatching(value.remove(0));
          }
        }));

    matchingRoomRepository.saveAll(notEnoughRoomList);
  }

  //룸이 없는 매칭끼리 최소인원 이상이 되도록 룸을 생성
  @Transactional
  public void createNewRoom() {
    var size = matchingMap.values().stream().mapToInt(List::size).min().orElse(0);

    for (int i = 0; i < size; i++) {
      var matchingList = new ArrayList<Matching>();
      matchingMap.forEach((key, value) -> {
        matchingList.add(value.remove(0));
      });
      var newRoom = MatchingRoom.createRoom(matchingList, chatRoomService.createChatRoom().getId());
      matchingRoomRepository.save(newRoom);
    }
  }

  //최소인원수를 만족한 룸을 찾아서 매칭을 시도
  public void joinRoom() {

    //frontend, backend, designer, planner순으로 룸에 들어가기
    joinRoomByType(PositionType.FRONTEND);
    joinRoomByType(PositionType.BACKEND);
    joinRoomByType(PositionType.DESIGNER);
    joinRoomByType(PositionType.PLANNER);
  }

  private void joinRoomByType(PositionType positionType) {
    var matchingList = matchingMap.get(positionType);
    var roomList = new ArrayList<>(matchingRoomList.stream()
        .filter(matchingRoom -> matchingRoom.isCanInsertPosition(positionType))
        .toList());

    matchingList.forEach(matching -> {
      var room = roomList.stream()
          .filter(matchingRoom ->
              matchingRoom.isCanInsertPosition(positionType) &&
                  matchingRoom.getRequiredSkillIds(positionType).retainAll(
                      matching.getUser().getUserInfo()
                          .getUserInfoSkills().stream()
                          .map(userInfoSkill -> userInfoSkill.getSkill().getId()).toList()
                  )
          )
          .findFirst()
          .orElse(null);

      if (room != null) {
        room.addMatching(matching);
        matchingRoomRepository.save(room);
        roomList.remove(room);
      }
    });
  }
}
