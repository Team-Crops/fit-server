package org.crops.fitserver.domain.region.dto.response;

import java.util.List;
import org.crops.fitserver.domain.region.dto.RegionDto;

public record GetRegionListResponse(List<RegionDto> regionList) {
  public static GetRegionListResponse of(List<RegionDto> regionList) {
    return new GetRegionListResponse(regionList);
  }
}
