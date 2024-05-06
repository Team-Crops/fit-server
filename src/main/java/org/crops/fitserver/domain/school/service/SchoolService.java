package org.crops.fitserver.domain.school.service;

import java.util.List;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.dto.SchoolDto;

public interface SchoolService {

  List<SchoolDto> getSchoolList();

  List<SchoolDto> getSchoolListByKeyword(String keyword, SchoolType schoolType);
}
