package org.crops.fitserver.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crops.fitserver.domain.region.domain.Region;

@AllArgsConstructor(staticName = "of")
@Getter
public class RegionDto {

  private Long id;
  private String displayName;

  public static RegionDto from(Region region){
    return RegionDto.of(region.getId(), region.getDisplayName());
  }
}
