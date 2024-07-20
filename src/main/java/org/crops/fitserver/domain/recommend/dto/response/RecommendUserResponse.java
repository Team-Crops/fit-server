package org.crops.fitserver.domain.recommend.dto.response;

import java.util.List;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;

public record RecommendUserResponse (
  List<RecommendUserDto> recommendUserList
) {

  public static RecommendUserResponse of(List<RecommendUserDto> recommendUserDtoList) {
    return new RecommendUserResponse(recommendUserDtoList);
  }
}
