package org.crops.fitserver.domain.skillset.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SkillRepositoryTest {

  private final TestEntityManager em;

  private final SkillRepository skillRepository;

  @Test
  public void exists_by_display_name() {
    // given
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    em.persist(skill);

    // when
    var result = skillRepository.existsByDisplayName("test");

    // then
    assertThat(result).isEqualTo(true);
  }

  @Test
  public void create_skill_failed_duplicate_error() {
    // given
    Skill skill1 = Skill.builder()
        .displayName("test")
        .build();
    em.persist(skill1);

    Skill skill2 = Skill.builder()
        .displayName("test")
        .build();

    // when
    ThrowingCallable saveDuplicateSkill = () -> skillRepository.saveAndFlush(skill2);

    // then
    assertThatThrownBy(saveDuplicateSkill).isInstanceOf(
        DataIntegrityViolationException.class);
  }

  @Test
  public void create_skill() {
    // given
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    // when
    var result = skillRepository.save(skill);

    // then
    assertThat(result.getId()).isNotNull();
  }

  @Test
  public void update_skill() {
    // given
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    em.persist(skill);

    // when
    skill.updateDisplayName("test_new");
    var result = skillRepository.save(skill);

    // then
    assertThat(result.getDisplayName()).isEqualTo("test_new");
  }

  @Test
  public void select_skill_list() {
    // given
    Skill skill1 = Skill.builder()
        .displayName("test1")
        .build();
    em.persist(skill1);
    Skill skill2 = Skill.builder()
        .displayName("test2")
        .build();
    em.persist(skill2);

    // when
    var result = skillRepository.findAll();

    // then
    assertThat(result).hasSize(2);
  }

  @Test
  public void delete_skill() {
    // given
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    skill = skillRepository.save(skill);
    var skillId = skill.getId();

    // when
    skillRepository.delete(skill);
    em.flush();

    var result = skillRepository.findById(skillId);

    // then
    assertThat(result).isEqualTo(Optional.empty());
  }
}
