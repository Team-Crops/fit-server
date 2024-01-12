package org.crops.fitserver.domain.skillset.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
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


}
