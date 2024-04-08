package org.crops.fitserver.domain.recommend.facade;

import java.util.List;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;

public interface RecommendFacade {

  List<RecommendUserDto> recommendUser(long userId, int randomSeed, RecommendUserRequest request);

  void likeUser(long likeUserId, long likedUserId, boolean like);
}
