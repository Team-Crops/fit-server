package org.crops.fitserver.domain.recommend.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.ArrayList;
import java.util.List;
import org.crops.fitserver.domain.recommend.dto.RecommendUserDto;
import org.crops.fitserver.domain.recommend.dto.response.RecommendUserResponse;
import org.crops.fitserver.domain.recommend.facade.RecommendFacade;
import org.crops.fitserver.util.MockMvcDocs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(RecommendController.class)
@DisplayName("[Auth][Controller] AuthController Test")
class RecommendControllerTest extends MockMvcDocs {

  @MockBean
  RecommendFacade recommendFacade;

  @Nested
  @DisplayName("[GET] Recommend User Test")
  class OAuthLoginTest {

    private static final String URL = "http://localhost:8080/v1/recommend/user";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 필터링 추천 성공")
      @Test
      void recommendUser() throws Exception {
        List<RecommendUserDto> response = new ArrayList<>();
        given(recommendFacade.recommendUser())
            .willReturn(RecommendUserResponse.of(response));

        // when
        var result = mockMvc.perform(
            get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("positionId", "1, 2, 3")
                .queryParam("skillId", "1")
                .queryParam("backgroundStatus", "HIGH_SCHOOL_GRADUATE") // 학력
                .queryParam("regionId", "1")
                .queryParam("projectCount", "3")
                .queryParam("activityHour","3") // 6, 9, 12
                .queryParam("likeUser", "true")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(RecommendController.class))
//            .andExpect(jsonPath("$.recommendUserList[0].userId").value(""))
//            .andExpect(jsonPath("$.recommendUserList[0].username").value(""))
//            .andExpect(jsonPath("$.recommendUserList[0].positionName").value(""))
//            .andExpect(jsonPath("$.recommendUserList[0].introduce").value(""))
//            .andExpect(jsonPath("$.recommendUserList[0].portfolioUrl").value(""))
//            .andExpect(jsonPath("$.recommendUserList[0].skill[0]").value(""))
//            .andExpect(jsonPath("$.recommendUserList[0].isLiked").value(""))
            .andDo(
                document("OAuth Login Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Recommend")
                        .summary("Recommend User api")
                        .description("팀원 추천 api")
                        .queryParameters(
                            parameterWithName("positionId").description("포지션 아이디"),
                            parameterWithName("skillId").description("스킬 아이디"),
                            parameterWithName("backgroundStatus").description("학력"),
                            parameterWithName("regionId").description("지역 아이디"),
                            parameterWithName("projectCount").description("프로젝트 수"),
                            parameterWithName("activityHour").description("활동 시간"),
                            parameterWithName("likeUser").description("좋아요 여부")
                        )
                        .responseSchema(
                            schema("RecommendUserResponse"))
//                        .responseFields(
//                            fieldWithPath("recommendUserList[].userId")
//                                .type(JsonFieldType.STRING)
//                                .description("User Id"),
//                            fieldWithPath("recommendUserList[].username")
//                                .type(JsonFieldType.STRING)
//                                .description("User Name"),
//                            fieldWithPath("recommendUserList[].positionName")
//                                .type(JsonFieldType.STRING)
//                                .description("Position Name"),
//                            fieldWithPath("recommendUserList[].introduce")
//                                .type(JsonFieldType.STRING)
//                                .description("User Introduce"),
//                            fieldWithPath("recommendUserList[].portfolioUrl")
//                                .type(JsonFieldType.STRING)
//                                .description("User Portfolio Url"),
//                            fieldWithPath("recommendUserList[].skill[]")
//                                .type(JsonFieldType.STRING)
//                                .description("User Skill"),
//                            fieldWithPath("recommendUserList[].isLiked")
//                                .type(JsonFieldType.BOOLEAN)
//                                .description("User Like Status"))
                        .build())));
      }
    }
  }

//  @Test
//  void likeUser() {
//
//  }
}