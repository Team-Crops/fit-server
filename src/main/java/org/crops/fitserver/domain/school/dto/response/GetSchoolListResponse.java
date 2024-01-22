package org.crops.fitserver.domain.school.dto.response;

import java.util.List;
import org.crops.fitserver.domain.school.dto.SchoolDto;

public record GetSchoolListResponse(List<SchoolDto> schoolList) {

  public static GetSchoolListResponse of(List<SchoolDto> schoolList) {
    return new GetSchoolListResponse(schoolList);
  }
}
