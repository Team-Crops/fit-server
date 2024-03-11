package org.crops.fitserver.domain.matching.service;

import org.crops.fitserver.domain.matching.dto.response.CreateMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingResponse;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;

public interface MatchingService {

  CreateMatchingResponse createMatching(Long userId);

  GetMatchingResponse getMatching(Long userId);

  GetMatchingRoomResponse getMatchingRoom(Long userId, Long roomId);
  void cancelMatching(Long userId);

}
