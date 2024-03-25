package org.crops.fitserver.domain.matching.service;

import java.util.List;
import org.crops.fitserver.domain.matching.dto.response.CreateMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.entity.Matching;

public interface MatchingService {

  CreateMatchingResponse createMatching(Long userId);

  GetMatchingResponse getMatching(Long userId);

  GetMatchingRoomResponse getMatchingRoom(Long userId, Long roomId);

  void completeMatching(Long userId, Long roomId);

  void cancelMatching(Long userId);

  List<Matching> expireMatchingAll();

  void forceOut(Long userId, Long roomId, Long forceOutUserId);
}
