package org.crops.fitserver.domain.recommend.facade;

import java.util.List;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;

public interface RecommendFacade {

  List<RecommendUserDto> recommendUser(Long userId, RecommendUserRequest request);

  void likeUser(Long likeUserId, Long likedUserId, Boolean like);
}
