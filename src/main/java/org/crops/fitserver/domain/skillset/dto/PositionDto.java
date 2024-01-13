package org.crops.fitserver.domain.skillset.dto;


import lombok.Builder;
import org.crops.fitserver.domain.skillset.domain.Position;

@Builder
public record PositionDto(Long id, String displayName) {

  public static PositionDto from(Position position) {
    return PositionDto.builder()
        .id(position.getId())
        .displayName(position.getDisplayName())
        .build();
  }
}
