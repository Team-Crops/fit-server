package org.crops.fitserver.domain.skillset.dto.response;

import java.util.List;
import org.crops.fitserver.domain.skillset.dto.PositionDto;

public record GetPositionListResponse(List<PositionDto> positionList) {

  public static GetPositionListResponse of(List<PositionDto> positionList) {
    return new GetPositionListResponse(positionList);
  }
}
