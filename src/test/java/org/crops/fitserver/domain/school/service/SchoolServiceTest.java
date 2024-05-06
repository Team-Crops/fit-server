package org.crops.fitserver.domain.school.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.domain.School;
import org.crops.fitserver.domain.school.repository.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {
  @InjectMocks
  private SchoolServiceImpl schoolService;

  @Mock
  private SchoolRepository schoolRepository;

  @Test
  public void getSchoolList_success() {
    // given
    given(schoolRepository.findAll()).willReturn(List.of(
        School.builder()
            .name("서울대학교")
            .build(),
        School.builder()
            .name("서울과학기술대학교")
            .build(),
        School.builder()
            .name("고려대학교")
            .build()
    ));

    // when
    var result = schoolService.getSchoolList();

    // then
    assertThat(result).hasSize(3);
  }

  @Test
  public void getSchoolListByName_success() {
    // given
    given(schoolRepository.findAllByNameStartsWithAndTypeEquals("서울", SchoolType.UNIVERSITY)).willReturn(List.of(
        School.builder()
            .name("서울대학교")
            .build(),
        School.builder()
            .name("서울과학기술대학교")
            .build()
    ));

    // when
    var result = schoolService.getSchoolListByKeyword("서울", SchoolType.UNIVERSITY);

    // then
    assertThat(result).hasSize(2);
  }
}
