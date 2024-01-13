package org.crops.fitserver.domain.skillset.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.skillset.dto.request.CreatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class SkillSetServiceTest {

  @InjectMocks
  SkillSetServiceImpl skillSetService;

  @Mock
  SkillRepository skillRepository;

  @Mock
  PositionRepository positionRepository;

  @Test
  public void create_skill_fail_duplicated() {
    // given
    CreateSkillRequest request = CreateSkillRequest.builder()
        .displayName("test")
        .build();
    given(skillRepository.findByDisplayName(any())).willReturn(
        Optional.of(Skill.builder()
            .displayName("test")
            .build())
    );

    // when
    ThrowingCallable result = () -> skillSetService.createSkill(request);

    // then
    assertThatThrownBy(result).isInstanceOf(BusinessException.class);
  }

  @Test
  public void create_skill() {
    // given
    CreateSkillRequest request = CreateSkillRequest.builder()
        .displayName("test")
        .build();
    given(skillRepository.save(any(Skill.class))).willReturn(Skill.builder()
        .displayName("test")
        .build());
    // when
    var result = skillSetService.createSkill(request);

    // then
    assertThat(result.displayName()).isEqualTo("test");
  }

  @Test
  public void create_skill_with_position() {
    // given
    CreateSkillRequest request = CreateSkillRequest.builder()
        .displayName("test")
        .positionIds(List.of(1L, 2L))
        .build();
    given(positionRepository.findAllById(any())).willReturn(List.of(
        Position.builder().id(1L).build(),
        Position.builder().id(2L).build()
    ));
    given(skillRepository.save(any(Skill.class))).willReturn(Skill.builder()
        .displayName("test")
        .build());
    given(positionRepository.saveAll(any())).willReturn(null);

    // when
    var result = skillSetService.createSkill(request);

    // then
    assertThat(result.displayName()).isEqualTo("test");
    Mockito.verify(positionRepository, Mockito.times(1)).saveAll(any());
  }

  @Test
  public void create_skill_with_position_fail_not_found() {
    // given
    CreateSkillRequest request = CreateSkillRequest.builder()
        .displayName("test")
        .positionIds(List.of(1L, 2L))
        .build();
    given(positionRepository.findAllById(any())).willReturn(List.of(
        Position.builder().id(1L).build()
    ));

    // when
    ThrowingCallable result = () -> skillSetService.createSkill(request);

    // then
    assertThatThrownBy(result).isInstanceOf(BusinessException.class);
  }

  @Test
  public void create_position() {
    // given
    CreatePositionRequest request = CreatePositionRequest.builder()
        .displayName("test")
        .build();
    given(positionRepository.save(any(Position.class))).willReturn(Position.builder()
        .displayName("test")
        .build());
    // when
    var result = skillSetService.createPosition(request);

    // then
    assertThat(result.displayName()).isEqualTo("test");
  }

  @Test
  public void create_position_fail_duplicated() {
    // given
    CreatePositionRequest request = CreatePositionRequest.builder()
        .displayName("test")
        .build();
    given(positionRepository.findByDisplayName(any())).willReturn(
        Optional.of(Position.builder()
            .displayName("test")
            .build())
    );

    // when
    ThrowingCallable result = () -> skillSetService.createPosition(request);

    // then
    assertThatThrownBy(result).isInstanceOf(BusinessException.class);
  }

  @Test
  public void create_position_with_skill() {
    // given
    CreatePositionRequest request = CreatePositionRequest.builder()
        .displayName("test")
        .skillIds(List.of(1L, 2L))
        .build();
    given(skillRepository.findAllById(any())).willReturn(List.of(
        Skill.builder().id(1L).build(),
        Skill.builder().id(2L).build()
    ));
    given(positionRepository.save(any(Position.class))).willReturn(Position.builder()
        .displayName("test")
        .build());

    // when
    var result = skillSetService.createPosition(request);

    // then
    assertThat(result.displayName()).isEqualTo("test");
    Mockito.verify(positionRepository, Mockito.times(2)).save(any());
  }

  @Test
  public void create_position_with_skill_fail_not_found() {
    // given
    CreatePositionRequest request = CreatePositionRequest.builder()
        .displayName("test")
        .skillIds(List.of(1L, 2L))
        .build();
    given(skillRepository.findAllById(any())).willReturn(List.of(
        Skill.builder().id(1L).build()
    ));

    // when
    ThrowingCallable result = () -> skillSetService.createPosition(request);

    // then
    assertThatThrownBy(result).isInstanceOf(BusinessException.class);
  }

  @Test
  public void get_position_list_success() {
    // given
    given(positionRepository.findAll()).willReturn(List.of(
        Position.builder().displayName("test").build()
    ));

    // when
    var result = skillSetService.getPostionList();

    // then
    assertThat(result).hasSize(1);
  }

  @Test
  public void get_skill_list_success() {
    // given
    given(skillRepository.findAll()).willReturn(List.of(
        Skill.builder().displayName("test").build()
    ));

    // when
    var result = skillSetService.getSkillList();

    // then
    assertThat(result).hasSize(1);
  }

  @Test
  public void get_skill_list_by_position_success() {
    // given
    given(positionRepository.findByPositionId(any()))
        .willReturn(List.of(
                Skill.builder().displayName("test").build()
            )
        );

    // when
    var result = skillSetService.getSkillListByPositionId(1L);

    // then
    assertThat(result).hasSize(1);
  }


}
