package org.crops.fitserver.domain.matching.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.config.UserBuildUtil;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.entity.Matching;
import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.matching.entity.MatchingRoom.MatchingRoomBuilder;
import org.crops.fitserver.domain.matching.repository.MatchingRepository;
import org.crops.fitserver.domain.matching.repository.MatchingRoomRepository;
import org.crops.fitserver.domain.project.repository.ProjectRepository;
import org.crops.fitserver.domain.user.domain.UserBlock;
import org.crops.fitserver.domain.user.repository.UserBlockRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

  @InjectMocks
  private MatchingServiceImpl sut;
  @Mock
  private MatchingRepository matchingRepository;
  @Mock
  private MatchingRoomRepository matchingRoomRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private UserBlockRepository userBlockRepository;

  @Nested
  @DisplayName("매칭 생성 테스트")
  class CreateMatchingTest {

    @Test
    void cannot_create_matching_when_user_is_blocked() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(userBlockRepository.findActiveBlock(user)).willReturn(
          Optional.of(UserBlock.create(user)));

      //when
      ThrowingCallable result = () -> sut.createMatching(user.getId());

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class)
          .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BLOCKED_USER_EXCEPTION);

    }

    @Test
    void cannot_create_matching_when_user_already_has_matching() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(userBlockRepository.findActiveBlock(user)).willReturn(Optional.empty());
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.of(Matching.create(user)));

      //when
      ThrowingCallable result = () -> sut.createMatching(user.getId());

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class)
          .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ALREADY_EXIST_MATCHING_EXCEPTION);

    }

    @Test
    void create_matching_successfully() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(userBlockRepository.findActiveBlock(user)).willReturn(Optional.empty());
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.empty());
      given(matchingRepository.save(Mockito.any(Matching.class))).willReturn(Matching.create(user));

      //when
      var result = sut.createMatching(user.getId());

      //then
      assertThat(result).isNotNull();

    }
  }

  @Nested
  @DisplayName("매칭 조회 테스트")
  class GetMatchingTest {

    @Test
    void cannot_get_matching_when_user_not_exist() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.empty());

      //when
      ThrowingCallable result = () -> sut.getMatching(user.getId());

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class);

    }

    @Test
    void cannot_get_matching_when_user_does_not_have_matching() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.empty());

      //when
      ThrowingCallable result = () -> sut.getMatching(user.getId());

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class);

    }

    @Test
    void get_matching_successfully() {
      //given
      var user = UserBuildUtil.buildUser().build();
      var matching = Matching.create(user);
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.of(matching));

      //when
      var result = sut.getMatching(user.getId());

      //then
      assertThat(result).isNotNull();

    }
  }

  @Nested
  @DisplayName("매칭방 조회 테스트")
  class GetMatchingRoomTest {

    private static final long hostUserId = 100L;

    private static MatchingRoomBuilder builderRoom() {
      return MatchingRoom.builder()
          .id(1L)
          .chatRoomId(1L)
          .hostUserId(hostUserId)
          .isCompleted(false)
          .completedAt(null);
    }

    @Test
    void cannot_get_matching_room_when_user_not_exist() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.empty());

      //when
      ThrowingCallable result = () -> sut.getMatchingRoom(user.getId(), 1L);

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class);

    }

    @Test
    void cannot_get_matching_room_when_user_does_not_have_matching() {
      //given
      var user = UserBuildUtil.buildUser().build();
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.empty());

      //when
      ThrowingCallable result = () -> sut.getMatchingRoom(user.getId(), 1L);

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class);

    }

    @Test
    void cannot_get_matching_room_when_matching_does_not_have_matching_room() {
      //given
      var user = UserBuildUtil.buildUser().build();
      var matching = Matching.create(user);
      var matchingRoom = builderRoom().id(1323L).build();
      matchingRoom.addMatching(matching);
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.of(matching));

      //when
      ThrowingCallable result = () -> sut.getMatchingRoom(user.getId(), 1L);

      //then
      assertThatThrownBy(result)
          .isInstanceOf(BusinessException.class);

    }

    @Test
    void get_matching_room_successfully() {
      //given
      var user = UserBuildUtil.buildUser().build();
      var matching = Matching.create(user);
      var matchingRoom = builderRoom().build();
      matchingRoom.addMatching(matching);
      given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
      given(matchingRepository.findActiveMatchingByUserAndStatus(user,
          MatchingStatus.getActiveStatusList()))
          .willReturn(Optional.of(matching));
      given(matchingRoomRepository.findWithMatchingMembersById(matchingRoom.getId())).willReturn(
          Optional.of(matchingRoom));

      //when
      var result = sut.getMatchingRoom(user.getId(), 1L);

      //then
      assertThat(result).isNotNull();

    }
  }


}