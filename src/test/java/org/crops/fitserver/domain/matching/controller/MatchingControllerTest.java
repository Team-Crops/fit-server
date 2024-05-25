package org.crops.fitserver.domain.matching.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.fields;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.crops.fitserver.domain.user.util.PrincipalDetailsUtil.getPrincipalDetails;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.config.MockMvcDocsWithLogin;
import org.crops.fitserver.config.UserBuildUtil;
import org.crops.fitserver.domain.matching.constant.MatchingStatus;
import org.crops.fitserver.domain.matching.dto.MatchingDto;
import org.crops.fitserver.domain.matching.dto.MatchingUserView;
import org.crops.fitserver.domain.matching.dto.request.ForceOutRequest;
import org.crops.fitserver.domain.matching.dto.request.ReadyMatchingRequest;
import org.crops.fitserver.domain.matching.dto.response.GetMatchingRoomResponse;
import org.crops.fitserver.domain.matching.service.MatchingService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(MatchingController.class)
@Slf4j
class MatchingControllerTest extends MockMvcDocsWithLogin {

  @MockBean
  private MatchingService matchingService;

  @Nested
  @DisplayName("매칭 생성/조회 테스트")
  class MatchingTest {

    private static final String URL = "/v1/matching";

    @DisplayName("매칭 생성 성공")
    @Test
    void create_when_dont_exist_matching() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      given(matchingService.createMatching(principal.getUserId())).willReturn(
          new MatchingDto(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
              MatchingStatus.WAITING));
      //when
      var result = mockMvc.perform(post(URL)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/createMatching",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 생성")
                      .summary("매칭 생성")
                      .responseSchema(Schema.schema("createMatchingResponse"))
                      .responseFields(
                          fields(
                              fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                  .description("유저 ID"),
                              fieldWithPath("roomId").type(JsonFieldType.NUMBER)
                                  .description("방 ID").optional(),
                              fieldWithPath("positionId").type(JsonFieldType.NUMBER)
                                  .description("포지션 ID"),
                              fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                  .description("생성일"),
                              fieldWithPath("expiredAt").type(JsonFieldType.STRING)
                                  .description("만료일"),
                              new EnumFields(MatchingStatus.class).withPath("status")
                                  .description("매칭 상태")
                          )
                      )
                      .build()
              )
          ));
    }

    @DisplayName("매칭 생성 실패 - 이미 매칭중")
    @Test
    void cannot_create_when_exist_matching() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      given(matchingService.createMatching(principal.getUserId())).willThrow(new BusinessException(
          ErrorCode.ALREADY_EXIST_MATCHING_EXCEPTION));
      //when
      var result = mockMvc.perform(post(URL)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isConflict())
          .andDo(document("matching/createMatching_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 생성 실패")
                      .summary("매칭 생성 실패")
                      .responseSchema(Schema.schema("errorResponse"))
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭 조회 성공")
    void get_own_matching() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      given(matchingService.getMatching(principal.getUserId())).willReturn(
          new MatchingDto(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
              MatchingStatus.WAITING));
      //when
      var result = mockMvc.perform(get(URL)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/getMatching",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 조회")
                      .summary("매칭 조회")
                      .responseSchema(Schema.schema("getMatchingResponse"))
                      .responseFields(
                          fields(
                              fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                  .description("유저 ID"),
                              fieldWithPath("roomId").type(JsonFieldType.NUMBER)
                                  .description("방 ID").optional(),
                              fieldWithPath("positionId").type(JsonFieldType.NUMBER)
                                  .description("포지션 ID"),
                              fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                  .description("생성일"),
                              fieldWithPath("expiredAt").type(JsonFieldType.STRING)
                                  .description("만료일"),
                              new EnumFields(MatchingStatus.class).withPath("status")
                                  .description("매칭 상태")
                          )
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭 조회 실패 - 매칭중이 아님")
    void cannot_get_when_dont_exist_matching() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      given(matchingService.getMatching(principal.getUserId())).willThrow(new BusinessException(
          ErrorCode.NOT_EXIST_MATCHING_EXCEPTION));
      //when
      var result = mockMvc.perform(get(URL)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isNotFound())
          .andDo(document("matching/getMatching_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 조회 실패")
                      .summary("매칭 조회 실패")
                      .responseSchema(Schema.schema("errorResponse"))
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }
  }

  @Nested
  @DisplayName("매칭방 테스트")
  class MatchingRoomTest {

    @Test
    @DisplayName("매칭방 조회 성공")
    void get_matching_room_when_client_is_participant() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}";
      given(matchingService.getMatchingRoom(principal.getUserId(), roomId)).willReturn(
          new GetMatchingRoomResponse(
              roomId, 1L, false, null, principal.getUserId(), List.of(
              new MatchingUserView(principal.getUserId(), 1L, user.getNickname(),
                  user.getProfileImageUrl(), true, true),
              new MatchingUserView(0L, 1L, user.getNickname(), user.getProfileImageUrl(), true,
                  false)
          )));
      //when
      var result = mockMvc.perform(get(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/getMatchingRoom",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭방 조회")
                      .summary("매칭방 조회")
                      .responseSchema(Schema.schema("getMatchingRoomResponse"))
                      .responseFields(
                          fields(
                              fieldWithPath("matchingRoomId").type(JsonFieldType.NUMBER)
                                  .description("방 ID"),
                              fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER)
                                  .description("채팅방 ID"),
                              fieldWithPath("isCompleted").type(JsonFieldType.BOOLEAN)
                                  .description("매칭 완료 여부. true: 매칭 모두 완료, false: 매칭 중"),
                              fieldWithPath("completedAt").type(JsonFieldType.STRING)
                                  .description("매칭 완료 일시. null: 매칭 중").optional(),
                              fieldWithPath("hostUserId").type(JsonFieldType.NUMBER)
                                  .description("임시 방장 ID"),
                              fieldWithPath("matchingUserList").type(JsonFieldType.ARRAY)
                                  .description("매칭 멤버 목록").optional(),
                              fieldWithPath("matchingUserList[].userId").type(JsonFieldType.NUMBER)
                                  .description("유저 ID"),
                              fieldWithPath("matchingUserList[].positionId").type(
                                      JsonFieldType.NUMBER)
                                  .description("포지션 ID"),
                              fieldWithPath("matchingUserList[].nickname").type(
                                      JsonFieldType.STRING)
                                  .description("유저 별명"),
                              fieldWithPath("matchingUserList[].profileImageUrl").type(
                                      JsonFieldType.STRING)
                                  .description("프로필 이미지 URL"),
                              fieldWithPath("matchingUserList[].isHost").type(JsonFieldType.BOOLEAN)
                                  .description("방장 여부"),
                              fieldWithPath("matchingUserList[].isReady").type(
                                      JsonFieldType.BOOLEAN)
                                  .description("준비 여부")
                          )
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭방 조회 실패 - 매칭방이 없음")
    void cannot_get_matching_room_when_dont_exist() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}";
      given(matchingService.getMatchingRoom(principal.getUserId(), roomId)).willThrow(
          new BusinessException(
              ErrorCode.NOT_EXIST_MATCHING_ROOM_EXCEPTION));
      //when
      var result = mockMvc.perform(get(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isNotFound())
          .andDo(document("matching/getMatchingRoom_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭방 조회 실패")
                      .summary("매칭방 조회 실패")
                      .responseSchema(Schema.schema("errorResponse"))
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }

  }

  @Nested
  @DisplayName("매칭 강제 퇴장 테스트")
  class MatchingForceOutTest {

    @Test
    @DisplayName("매칭 강제 퇴장 성공")
    void force_out_when_client_is_host() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}/force-out";
      var forceOutRequest = new ForceOutRequest(2L);
      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(forceOutRequest))
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/forceOut",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("강제퇴장")
                      .summary("강제퇴장")
                      .requestSchema(Schema.schema("forceOutRequest"))
                      .requestFields(
                          fieldWithPath("userId").type(JsonFieldType.NUMBER)
                              .description("강제퇴장 대상자 ID")
                      )
                      .build()
              )
          ));

    }

    @Test
    @DisplayName("매칭 강제 퇴장 실패 - 방장이 아님")
    void cannot_force_out_when_client_is_not_host() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var forceOutUserId = 2L;
      var url = "/v1/matching/room/{roomId}/force-out";
      willThrow(new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION)).willDoNothing().given(
          matchingService).forceOut(principal.getUserId(), roomId, forceOutUserId);

      var forceOutRequest = new ForceOutRequest(forceOutUserId);
      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(forceOutRequest))
          .with(user(principal))
          .with(csrf())
      );

      //then
      result.andExpect(status().isForbidden())
          .andDo(document("matching/forceOut_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("강제퇴장 실패 - 방장이 아님")
                      .summary("강제퇴장 실패 - 방장이 아님")
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭 강제 퇴장 실패 - 대상자가 존재하지 않음")
    void cannot_force_out_when_target_dont_exist() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var forceOutUserId = 2L;
      var url = "/v1/matching/room/{roomId}/force-out";
      willThrow(new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION)).willDoNothing().given(
          matchingService).forceOut(principal.getUserId(), roomId, forceOutUserId);

      var forceOutRequest = new ForceOutRequest(forceOutUserId);
      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(forceOutRequest))
          .with(user(principal))
          .with(csrf())
      );

      //then
      result.andExpect(status().isForbidden())
          .andDo(document("matching/forceOut_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("강제퇴장 실패 - 대상자가 존재하지 않음")
                      .summary("강제퇴장 실패 - 대상자가 존재하지 않음")
                      .responseSchema(Schema.schema("forceOutRequest"))
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }
  }

  @Nested
  @DisplayName("매칭 준비/완료 테스트")
  class MatchingReadyAndCompleteTest {

    @Test
    @DisplayName("매칭 준비 성공")
    void ready_matching_successfully() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}/ready";
      var readyMatchingRequest = new ReadyMatchingRequest(true);
      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(readyMatchingRequest))
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/readyMatching",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 준비")
                      .summary("매칭 준비")
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭 준비 실패 - 방장은 준비할 수 없음")
    void cannot_ready_when_client_is_host() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}/ready";
      willThrow(new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION)).willDoNothing().given(
          matchingService).ready(principal.getUserId(), roomId);
      var readyMatchingRequest = new ReadyMatchingRequest(true);

      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(readyMatchingRequest))
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isForbidden())
          .andDo(document("matching/readyMatching_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 준비 실패 - 방장은 준비할 수 없음")
                      .summary("매칭 준비 실패 - 방장은 준비할 수 없음")
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭 완료 성공")
    void complete_matching_when_client_is_host() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}/complete";
      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/completeMatching",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 완료")
                      .summary("매칭 완료")
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("매칭 완료 실패 - 방장이 아님")
    void cannot_complete_when_client_is_not_host() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var roomId = 1L;
      var url = "/v1/matching/room/{roomId}/complete";
      willThrow(new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION)).willDoNothing().given(
          matchingService).complete(principal.getUserId(), roomId);
      //when
      var result = mockMvc.perform(post(url, roomId)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isForbidden())
          .andDo(document("matching/completeMatching_fail",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 완료 실패 - 방장이 아님")
                      .summary("매칭 완료 실패 - 방장이 아님")
                      .responseFields(
                          fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                          fieldWithPath("message").type(JsonFieldType.STRING)
                              .description("에러 메시지")
                      )
                      .build()
              )
          ));
    }
  }

  @Nested
  @DisplayName("매칭 취소 테스트")
  class MatchingCancelTest {

    @Test
    @DisplayName("매칭 취소 성공")
    void cancel_matching() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var url = "/v1/matching/cancel";
      //when
      var result = mockMvc.perform(post(url)
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
          .andDo(document("matching/cancelMatching",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("matching")
                      .description("매칭 취소")
                      .summary("매칭 취소")
                      .build()
              )
          ));

    }
  }
  @Nested
  @DisplayName("매칭 대기방 취소 테스트")
  class MatchingRoomCancelTest {

    @Test
    @DisplayName("매칭 대기방 취소 성공")
    void cancel_matching_room() throws Exception {
      //given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var url = "/v1/matching/room/{roomId}/cancel";
      var roomId = 1L;
      //when
      var result = mockMvc.perform(post(url, roomId)
              .contentType(MediaType.APPLICATION_JSON)
              .with(user(principal))
              .with(csrf())
      );
      //then
      result.andExpect(status().isOk())
              .andDo(document("matching/cancelMatching",
                      resource(
                              ResourceSnippetParameters.builder()
                                      .tag("matching")
                                      .description("매칭방 대기방 나가기")
                                      .summary("매칭 대기방 나가기")
                                      .build()
                      )
              ));

    }
  }
}