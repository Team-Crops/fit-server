package org.crops.fitserver.domain.matching.dto.response;

import org.crops.fitserver.domain.matching.dto.MatchingDto;
import org.crops.fitserver.domain.matching.entity.Matching;

public record CreateMatchingResponse(
    MatchingDto matching
) {

  public static CreateMatchingResponse from(Matching matching) {
    return new CreateMatchingResponse(MatchingDto.from(matching));
  }
}
