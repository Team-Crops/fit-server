package org.crops.fitserver.domain.matching.dto.response;

import org.crops.fitserver.domain.matching.dto.MatchingDto;
import org.crops.fitserver.domain.matching.entity.Matching;

public record GetMatchingResponse(
    MatchingDto matching
) {

  public static GetMatchingResponse from(Matching matching) {
    return new GetMatchingResponse(MatchingDto.from(matching));
  }
}
