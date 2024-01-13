package org.crops.fitserver.domain.school.repository;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.school.entity.School;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@DataJpaTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@EnableJpaAuditing
public class SchoolRepositoryTest {
  private final SchoolRepository schoolRepository;

  @Test
  public void getSchoolByKeyword() {
    // given
    var school1 = School.builder()
        .name("서울대학교")
        .build();
    var school2 = School.builder()
        .name("서울과학기술대학교")
        .build();
    var school3 = School.builder()
        .name("고려대학교")
        .build();
    schoolRepository.save(school1);
    schoolRepository.save(school2);
    schoolRepository.save(school3);
    // when
    var result = schoolRepository.findAllByNameStartsWith("서울");
    // then
    assertThat(result).hasSize(2);
  }
}
