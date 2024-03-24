package org.crops.fitserver.domain.recommend.facade;

import org.crops.fitserver.domain.recommend.dto.response.RecommendUserResponse;

public interface RecommendFacade {

  RecommendUserResponse recommendUser();

  void likeUser();
}
