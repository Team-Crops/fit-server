package org.crops.fitserver.domain.skillset.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.config.PersistenceConfig;
import org.crops.fitserver.config.QueryDslTestConfig;
import org.crops.fitserver.domain.skillset.constant.PositionType;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@DataJpaTest
@Import({QueryDslTestConfig.class, PersistenceConfig.class})
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PositionRepositoryTest {

  private final TestEntityManager em;
  private final PositionRepository positionRepository;
  private final SkillRepository skillRepository;

  @Test
  public void create_position() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .displayNameEn("test")
        .type(PositionType.BACKEND)
        .imageUrl("test")
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
        .displayNameEn("test")
        .type(PositionType.BACKEND)
        .imageUrl("test")
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
        .displayNameEn("test1")
        .type(PositionType.BACKEND)
        .imageUrl("test")
        .build();
    positionRepository.save(position1);

    Position position2 = Position.builder()
        .displayName("test2")
        .displayNameEn("test2")
        .type(PositionType.BACKEND)
        .imageUrl("test")
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
        .displayNameEn("test")
        .type(PositionType.BACKEND)
        .imageUrl("test")
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
        .type(PositionType.BACKEND)
        .displayNameEn("test")
        .imageUrl("test")
        .build();
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    skillRepository.save(skill);
    em.flush();


    // when
    position.addSkill(skill);
    var result = positionRepository.save(position);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getSkillSets().size()).isEqualTo(1);
  }

  @Test
  public void remove_skill_from_position() {
    // given
    Position position = Position.builder()
        .displayName("test")
        .displayNameEn("test")
        .type(PositionType.BACKEND)
        .imageUrl("test")
        .build();
    Skill skill = Skill.builder()
        .displayName("test")
        .build();

    skillRepository.save(skill);
    position.addSkill(skill);
    positionRepository.save(position);

    // when
    position.removeSkill(skill);
    var result = positionRepository.save(position);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getSkillSets().size()).isEqualTo(0);
  }

  @Test
  public void delete_skill() {
    // given
    Position position = Position.builder()
        .displayName("테스트")
        .displayNameEn("test")
        .type(PositionType.BACKEND)
        .imageUrl("test")
        .build();
    Skill skill = Skill.builder()
        .displayName("test")
        .build();
    skill = skillRepository.save(skill);

    position.addSkill(skill);
    position = positionRepository.save(position);
    em.flush();
    em.clear();

    // when
    skillRepository.deleteById(skill.getId());
    em.flush();


    var result = positionRepository.findWithSkills(position.getId()).get();

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getSkillSets().size()).isEqualTo(0);
  }


}
