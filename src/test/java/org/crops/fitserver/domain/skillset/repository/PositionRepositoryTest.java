package org.crops.fitserver.domain.skillset.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class PositionRepositoryTest {

  private final TestEntityManager em;
  private final PositionRepository positionRepository;
  private final SkillRepository skillRepository;

  @Test
  public void create_position() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .build();
    // when
    var result = positionRepository.save(position);

    // then
    assertThat(result.getId()).isNotNull();
  }

  @Test
  public void update_position() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .build();
    positionRepository.save(position);

    // when
    position.updateDisplayName("test_new");
    var result = positionRepository.save(position);

    // then
    assertThat(result.getDisplayName()).isEqualTo("test_new");
  }

  @Test
  public void select_position_list() {
    // given
    Position position1 = Position.builder()
        .displayName("test1")
        .build();
    positionRepository.save(position1);

    Position position2 = Position.builder()
        .displayName("test2")
        .build();
    positionRepository.save(position2);

    // when
    var result = positionRepository.findAll();

    // then
    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  public void delete_position() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .build();
    position = positionRepository.save(position);
    var positionId = position.getId();

    // when
    positionRepository.delete(position);
    em.flush();

    var result = positionRepository.findById(positionId);

    // then
    assertThat(result).isEqualTo(Optional.empty());
  }

  @Test
  public void add_skill() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .build();
    Skill skill = Skill.builder()
        .displayName("test")
        .build();

    position.addSkill(skill);
    // when
    var result = positionRepository.save(position);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getSkills().size()).isEqualTo(1);
  }

  @Test
  public void remove_skill() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .build();
    Skill skill = Skill.builder()
        .displayName("test")
        .build();

    position.addSkill(skill);
    positionRepository.save(position);

    // when
    position.removeSkill(skill);
    var result = positionRepository.save(position);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getSkills().size()).isEqualTo(0);
  }

  @Test
  public void delete_skill() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .build();
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    skill = skillRepository.save(skill);

    position.addSkill(skill);
    position = positionRepository.save(position);
    em.flush();
    em.clear();

    skillRepository.deleteById(skill.getId());
    em.flush();

    // when
    var result = positionRepository.findWithSkills(position.getId()).get();

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getSkills().size()).isEqualTo(0);
  }


}
