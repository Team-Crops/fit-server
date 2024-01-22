package org.crops.fitserver.domain.region.dto;

import org.crops.fitserver.domain.region.domain.Region;

public record RegionDto(Long id, String displayName) {

  public static RegionDto from(Region region) {
    return RegionDto.of(region.getId(), region.getDisplayName());
  }

  public static RegionDto of(Long id, String displayName) {
    return new RegionDto(id, displayName);
  }
}
