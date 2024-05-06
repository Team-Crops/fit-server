package org.crops.fitserver.domain.school.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.dto.SchoolDto;
import org.crops.fitserver.domain.school.repository.SchoolRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
  private final SchoolRepository schoolRepository;

  @Override
  public List<SchoolDto> getSchoolList() {
    return schoolRepository.findAll().stream()
        .map(SchoolDto::from)
        .toList();
  }

  @Override
  public List<SchoolDto> getSchoolListByKeyword(String keyword, SchoolType type) {
    if(keyword == null || type == null) {
      throw new BusinessException(ErrorCode.INVALID_ACCESS_EXCEPTION);
    }
    return schoolRepository.findAllByNameStartsWithAndTypeEquals(keyword, type).stream()
        .map(SchoolDto::from)
        .toList();
  }
}
