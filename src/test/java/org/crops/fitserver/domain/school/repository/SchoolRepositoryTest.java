package org.crops.fitserver.domain.school.repository;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.config.PersistenceConfig;
import org.crops.fitserver.config.QueryDslTestConfig;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.domain.School;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@DataJpaTest
@Import({QueryDslTestConfig.class, PersistenceConfig.class})
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SchoolRepositoryTest {
  private final SchoolRepository schoolRepository;

  @Test
  public void getSchoolByKeyword() {
    // given
    var school1 = School.builder()
        .name("서울대학교")
        .type(SchoolType.UNIVERSITY)
        .build();
    var school2 = School.builder()
        .name("서울과학기술대학교")
        .type(SchoolType.UNIVERSITY)
        .build();
    var school3 = School.builder()
        .name("고려대학교")
        .type(SchoolType.UNIVERSITY)
        .build();
    schoolRepository.save(school1);
    schoolRepository.save(school2);
    schoolRepository.save(school3);
    // when
    var result = schoolRepository.findAllByNameStartsWithAndTypeEquals("서울", SchoolType.UNIVERSITY);
    // then
    assertThat(result).hasSize(2);
  }
}
