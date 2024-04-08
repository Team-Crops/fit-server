package org.crops.fitserver.domain.matching.service;

import java.util.List;
import org.crops.fitserver.domain.matching.dto.MatchingDto;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.entity.Matching;

public interface MatchingService {

  MatchingDto createMatching(Long userId);

  MatchingDto getMatching(Long userId);

  void cancel(Long userId);

  GetMatchingRoomResponse getMatchingRoom(Long userId, Long roomId);

  void ready(Long userId, Long roomId);

  void complete(Long userId, Long roomId);

  void exit(Long userId, Long roomId);

  List<Matching> expireMatchingAll();

  void forceOut(Long userId, Long roomId, Long forceOutUserId);
}
