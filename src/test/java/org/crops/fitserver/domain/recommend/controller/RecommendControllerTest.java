package org.crops.fitserver.domain.recommend.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Arrays;
import java.util.List;
import org.crops.fitserver.config.MockMvcDocsWithLogin;
import org.crops.fitserver.domain.recommend.dto.LikeUserRequest;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.request.RecommendUserRequest;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(RecommendController.class)
@DisplayName("[Auth][Controller] AuthController Test")
class RecommendControllerTest extends MockMvcDocsWithLogin {

  @MockBean
  RecommendFacade recommendFacade;

  @Nested
  @DisplayName("[GET] Recommend User Test")
  class RecommendUserTest {

    private static final String URL = "/v1/recommend/user";
    User user1 = User.builder()
        .id(1L)
        .username("username1")
        .profileImageUrl("profileImageUrl1")
        .userInfo(UserInfo.builder()
                .position(Position.builder().id(1L).displayName("positionName1").build())
                .introduce("introduce1")
                .build())
        .build();
    User user2 = User.builder()
        .id(2L)
        .username("username2")
        .profileImageUrl("profileImageUrl2")
        .userInfo(UserInfo.builder()
                .position(Position.builder().id(2L).displayName("positionName2").build())
                .introduce("introduce2")
                .build())
        .build();

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 필터링 추천 성공")
      @Test
      void recommendUser() throws Exception {
        // given
        List<User> users = Arrays.asList(user1, user2);
        users
            .forEach(user ->
                user
                    .getUserInfo()
                    .addSkill(Skill.builder().id(1L).build()));

        List<RecommendUserDto> list = users
            .stream()
            .map(user -> RecommendUserDto.of(user, true))
            .toList();
        given(recommendFacade.recommendUser(anyLong(), any(RecommendUserRequest.class)))
            .willReturn(list);

        // when
        var result = mockMvc.perform(
            get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(loginPrincipal))
                .with(csrf())
                .queryParam("liked", "true")
                .queryParam("positionId", "1, 2, 3")
                .queryParam("skillId", "1, 2, 3")
                .queryParam("backgroundStatus", "HIGH_SCHOOL_GRADUATE") // 학력
                .queryParam("regionId", "1")
                .queryParam("projectCount", "3")
                .queryParam("activityHour","3, 6") // 6, 9, 12
                .queryParam("page", "1")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(RecommendController.class))
            .andDo(
                document("Recommend User Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Recommend")
                        .summary("Recommend User api")
                        .description("팀원 추천 api")
                        .queryParameters(
                            parameterWithName("positionId")
                                .description("포지션 아이디")
                                .optional(),
                            parameterWithName("skillId").
                                description("스킬 아이디")
                                .optional(),
                            parameterWithName("backgroundStatus").
                                description("학력")
                                .optional(),
                            parameterWithName("regionId").
                                description("지역 아이디")
                                .optional(),
                            parameterWithName("projectCount").
                                description("프로젝트 수")
                                .optional(),
                            parameterWithName("activityHour").
                                description("활동 시간")
                                .defaultValue("3, 6, 9, 12, 24")
                                .optional(),
                            parameterWithName("liked").
                                description("좋아요 여부")
                                .optional(),
                            parameterWithName("page").
                                description("페이지")
                        )
                        .responseSchema(
                            schema("RecommendUserResponse"))
                        .responseFields(
                            fieldWithPath("recommendUserList[].userSummary.userId")
                                .type(JsonFieldType.NUMBER)
                                .description("User Id"),
                            fieldWithPath("recommendUserList[].userSummary.username")
                                .type(JsonFieldType.STRING)
                                .description("User Name"),
                            fieldWithPath("recommendUserList[].userSummary.positionId")
                                .type(JsonFieldType.NUMBER)
                                .description("Position Id"),
                            fieldWithPath("recommendUserList[].userSummary.introduce")
                                .type(JsonFieldType.STRING)
                                .description("User Introduce"),
                            fieldWithPath("recommendUserList[].userSummary.profileImageUrl")
                                .type(JsonFieldType.STRING)
                                .description("User Profile Image Url"),
                            fieldWithPath("recommendUserList[].userSummary.skillIdList")
                                .type(JsonFieldType.ARRAY)
                                .description("User Skill"),
                            fieldWithPath("recommendUserList[].isLiked")
                                .type(JsonFieldType.BOOLEAN)
                                .description("User Like Status"))
                        .build())));
      }
    }
  }

  @Nested
  @DisplayName("[POST] Like User Test")
  class LikeUserTest {

    private static final String URL = "/v1/recommend/like/user";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 유저 좋아요 성공")
      @Test
      void userlike() throws Exception {
        // given
        long likedUserId = 2L;
        boolean like = true;
        LikeUserRequest request = new LikeUserRequest(likedUserId, like);

        // when
        var result = mockMvc.perform(
            post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(loginPrincipal))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent())
            .andExpect(handler().handlerType(RecommendController.class))
            .andDo(
                document("Like User Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Recommend")
                        .summary("Like User api")
                        .description("유저 좋아요 api")
                        .requestSchema(
                            schema("LikeUserRequest"))
                        .requestFields(
                            fieldWithPath("userId")
                                .type(JsonFieldType.NUMBER)
                                .description("좋아요, 좋아요 취소 User Id"),
                            fieldWithPath("like")
                                .type(JsonFieldType.BOOLEAN)
                                .description("Like Status"))
                        .build())));
      }
    }
  }
}