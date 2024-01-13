package org.crops.fitserver.domain.school.dto;

import lombok.Builder;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.entity.School;

@Builder
public record SchoolDto (Long id, String name, SchoolType type){
  public static SchoolDto from(School school) {
    return SchoolDto.builder()
        .id(school.getId())
        .name(school.getName())
        .type(school.getType())
        .build();
  }
}
