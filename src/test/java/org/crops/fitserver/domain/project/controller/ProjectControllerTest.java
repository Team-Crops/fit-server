package org.crops.fitserver.domain.project.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.fields;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.crops.fitserver.domain.user.util.PrincipalDetailsUtil.getPrincipalDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.config.MockMvcDocs;
import org.crops.fitserver.config.UserBuildUtil;
import org.crops.fitserver.domain.project.constant.ProjectStatus;
import org.crops.fitserver.domain.project.dto.ProjectDto;
import org.crops.fitserver.domain.project.dto.ProjectMemberDto;
import org.crops.fitserver.domain.project.dto.request.UpdateProjectRequest;
import org.crops.fitserver.domain.project.dto.response.GetProjectListResponse;
import org.crops.fitserver.domain.project.service.ProjectService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest(ProjectController.class)
@Slf4j
class ProjectControllerTest extends MockMvcDocs {

  @MockBean
  private ProjectService projectService;

  @Override
  @BeforeEach
  protected void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .apply(
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withResponseDefaults(Preprocessors.prettyPrint())
        )
        .alwaysDo(print())
        .build();
  }

  @Nested
  @DisplayName("프로젝트 리스트 가져오기")
  class GetProjectList {

    private final String URL = "/v1/project";

    @Test
    @DisplayName("프로젝트 리스트를 가져온다.")
    void success() throws Exception {
      // given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      given(projectService.getProjectList(user.getId())).willReturn(
          GetProjectListResponse.from(
              (List.of(
                  ProjectDto.builder()
                      .projectId(1L)
                      .projectName("프로젝트1")
                      .projectStatus(ProjectStatus.PROJECT_COMPLETE)
                      .chatRoomId(1L)
                      .createdAt(LocalDateTime.now())
                      .completedAt(LocalDateTime.now())
                      .projectMemberList(List.of(
                          ProjectMemberDto.builder()
                              .userId(principal.getUserId())
                              .username(user.getUsername())
                              .positionId(user.getUserInfo().getPosition().getId())
                              .profileImageUrl(user.getProfileImageUrl())
                              .build(),
                          ProjectMemberDto.builder()
                              .userId(2L)
                              .username("user2")
                              .positionId(2L)
                              .profileImageUrl("profile2")
                              .build()
                      ))
                      .build(),
                  ProjectDto.builder()
                      .projectId(2L)
                      .projectName("프로젝트2")
                      .projectStatus(ProjectStatus.PROJECT_IN_PROGRESS)
                      .chatRoomId(1L)
                      .createdAt(LocalDateTime.now())
                      .projectMemberList(List.of(
                          ProjectMemberDto.builder()
                              .userId(principal.getUserId())
                              .username(user.getUsername())
                              .positionId(user.getUserInfo().getPosition().getId())
                              .profileImageUrl(user.getProfileImageUrl())
                              .build()
                      ))
                      .build()
              )
              )
          ));
      // when
      var result = mockMvc.perform(get(URL, principal.getUserId())
          .contentType(MediaType.APPLICATION_JSON)
          .with(user(principal))
          .with(csrf())
      );
      // then
      result.andExpect(status().isOk())
          .andDo(document("get-project-list",
                  resource(
                      ResourceSnippetParameters.builder()
                          .tag("project")
                          .summary("내 프로젝트 리스트 조회")
                          .description("내 프로젝트 리스트를 가져온다.")
                          .responseSchema(Schema.schema("getProjectListResponse"))
                          .responseFields(
                              fields(fieldWithPath("projectList").type(JsonFieldType.ARRAY)
                                      .description("프로젝트 리스트"),
                                  fieldWithPath("projectList[].projectId").type(JsonFieldType.NUMBER)
                                      .description("프로젝트 ID"),
                                  fieldWithPath("projectList[].projectName").type(JsonFieldType.STRING)
                                      .description("프로젝트 이름"),
                                  fieldWithPath("projectList[].projectMemberList").type(
                                      JsonFieldType.ARRAY).description("프로젝트 멤버 리스트"),
                                  fieldWithPath("projectList[].projectMemberList[].userId").type(
                                      JsonFieldType.NUMBER).description("멤버 ID"),
                                  fieldWithPath("projectList[].projectMemberList[].username").type(
                                      JsonFieldType.STRING).description("멤버 이름"),
                                  fieldWithPath(
                                      "projectList[].projectMemberList[].profileImageUrl").type(
                                      JsonFieldType.STRING).description("멤버 프로필 이미지 URL"),
                                  fieldWithPath("projectList[].projectMemberList[].positionId").type(
                                      JsonFieldType.NUMBER).description("멤버 포지션 ID"),
                                  fieldWithPath("projectList[].chatRoomId").type(JsonFieldType.NUMBER)
                                      .description("채팅방 ID"),
                                  fieldWithPath("projectList[].createdAt").type(JsonFieldType.STRING)
                                      .description("프로젝트 생성일"),
                                  fieldWithPath("projectList[].completedAt").type(JsonFieldType.STRING)
                                      .description("프로젝트 완료일").optional(),
                                  new EnumFields(ProjectStatus.class).withPath(
                                          "projectList[].projectStatus")
                                      .description("프로젝트 상태")
                              )
                          )
                          .build()
                  )
              )
          );
    }
  }

  @Nested
  @DisplayName("프로젝트 수정하기")
  class UpdateProject {

    private final String URL = "/v1/project/{projectId}";

    @Test
    @DisplayName("프로젝트의 이름을 수정")
    void successChangeName() throws Exception {
      // given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var projectId = 1L;
      var updateProjectRequest = new UpdateProjectRequest("new_name_project", null);

      given(projectService.updateProject(user.getId(), projectId, updateProjectRequest)).willReturn(
          ProjectDto.builder()
              .projectId(projectId)
              .projectName(updateProjectRequest.projectName())
              .projectStatus(ProjectStatus.PROJECT_IN_PROGRESS)
              .chatRoomId(1L)
              .createdAt(LocalDateTime.now())
              .projectMemberList(List.of(
                  ProjectMemberDto.builder()
                      .userId(principal.getUserId())
                      .username(user.getUsername())
                      .positionId(user.getUserInfo().getPosition().getId())
                      .profileImageUrl(user.getProfileImageUrl())
                      .build(),
                  ProjectMemberDto.builder()
                      .userId(2L)
                      .username("user2")
                      .positionId(2L)
                      .profileImageUrl("profile2")
                      .build()
              ))
              .build()
      );
      // when
      var result = mockMvc.perform(patch(URL, principal.getUserId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateProjectRequest))
          .with(user(principal))
          .with(csrf())
      );
      // then
      result.andExpect(status().isOk())
          .andDo(document("update-project",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("project")
                      .summary("프로젝트 수정")
                      .description("프로젝트의 이름을 수정한다.")
                      .requestSchema(Schema.schema("updateProjectRequest"))
                      .requestFields(
                          fieldWithPath("projectName").type(JsonFieldType.STRING)
                              .description("프로젝트 이름").optional(),
                          new EnumFields(ProjectStatus.class).withPath("projectStatus")
                              .description("프로젝트 상태").optional()
                      )
                      .responseSchema(Schema.schema("updateProjectResponse"))
                      .responseFields(
                          fields(fieldWithPath("project").type(JsonFieldType.OBJECT)
                                  .description("프로젝트 정보"),
                              fieldWithPath("project.projectId").type(JsonFieldType.NUMBER)
                                  .description("프로젝트 ID"),
                              fieldWithPath("project.projectName").type(JsonFieldType.STRING)
                                  .description("프로젝트 이름"),
                              fieldWithPath("project.projectMemberList").type(JsonFieldType.ARRAY)
                                  .description("프로젝트 멤버 리스트"),
                              fieldWithPath("project.projectMemberList[].userId").type(
                                      JsonFieldType.NUMBER)
                                  .description("멤버 ID"),
                              fieldWithPath("project.projectMemberList[].username").type(
                                      JsonFieldType.STRING)
                                  .description("멤버 이름"),
                              fieldWithPath("project.projectMemberList[].profileImageUrl").type(
                                  JsonFieldType.STRING).description("멤버 프로필 이미지 URL"),
                              fieldWithPath("project.projectMemberList[].positionId").type(
                                      JsonFieldType.NUMBER)
                                  .description("멤버 포지션 ID"),
                              fieldWithPath("project.chatRoomId").type(JsonFieldType.NUMBER)
                                  .description("채팅방 ID"),
                              fieldWithPath("project.createdAt").type(JsonFieldType.STRING)
                                  .description("프로젝트 생성일"),
                              fieldWithPath("project.completedAt").type(JsonFieldType.STRING)
                                  .description("프로젝트 완료일").optional(),
                              new EnumFields(ProjectStatus.class).withPath("project.projectStatus")
                                  .description("프로젝트 상태")
                          )
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("프로젝트의 상태를 수정")
    void successChangeStatus() throws Exception {
      // given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var projectId = 1L;
      var updateProjectRequest = new UpdateProjectRequest(null, ProjectStatus.PROJECT_COMPLETE);

      given(projectService.updateProject(user.getId(), projectId, updateProjectRequest)).willReturn(
          ProjectDto.builder()
              .projectId(projectId)
              .projectName("project_name")
              .projectStatus(updateProjectRequest.projectStatus())
              .chatRoomId(1L)
              .createdAt(LocalDateTime.now())
              .completedAt(LocalDateTime.now())
              .projectMemberList(List.of(
                  ProjectMemberDto.builder()
                      .userId(principal.getUserId())
                      .username(user.getUsername())
                      .positionId(user.getUserInfo().getPosition().getId())
                      .profileImageUrl(user.getProfileImageUrl())
                      .build(),
                  ProjectMemberDto.builder()
                      .userId(2L)
                      .username("user2")
                      .positionId(2L)
                      .profileImageUrl("profile2")
                      .build()
              ))
              .build()
      );
      // when
      var result = mockMvc.perform(patch(URL, principal.getUserId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateProjectRequest))
          .with(user(principal))
          .with(csrf())
      );
      // then
      result.andExpect(status().isOk())
          .andDo(document("update-project",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("project")
                      .summary("프로젝트 수정")
                      .description("프로젝트의 상태를 수정한다.")
                      .requestSchema(Schema.schema("updateProjectRequest"))
                      .requestFields(
                          fieldWithPath("projectName").type(JsonFieldType.STRING)
                              .description("프로젝트 이름").optional(),
                          new EnumFields(ProjectStatus.class).withPath("projectStatus")
                              .description("프로젝트 상태").optional()
                      )
                      .responseSchema(Schema.schema("updateProjectResponse"))
                      .responseFields(
                          fields(fieldWithPath("project").type(JsonFieldType.OBJECT)
                                  .description("프로젝트 정보"),
                              fieldWithPath("project.projectId").type(JsonFieldType.NUMBER)
                                  .description("프로젝트 ID"),
                              fieldWithPath("project.projectName").type(JsonFieldType.STRING)
                                  .description("프로젝트 이름"),
                              fieldWithPath("project.projectMemberList").type(JsonFieldType.ARRAY)
                                  .description("프로젝트 멤버 리스트"),
                              fieldWithPath("project.projectMemberList[].userId").type(
                                      JsonFieldType.NUMBER)
                                  .description("멤버 ID"),
                              fieldWithPath("project.projectMemberList[].username").type(
                                      JsonFieldType.STRING)
                                  .description("멤버 이름"),
                              fieldWithPath("project.projectMemberList[].profileImageUrl").type(
                                  JsonFieldType.STRING).description("멤버 프로필 이미지 URL"),
                              fieldWithPath("project.projectMemberList[].positionId").type(
                                      JsonFieldType.NUMBER)
                                  .description("멤버 포지션 ID"),
                              fieldWithPath("project.chatRoomId").type(JsonFieldType.NUMBER)
                                  .description("채팅방 ID"),
                              fieldWithPath("project.createdAt").type(JsonFieldType.STRING)
                                  .description("프로젝트 생성일"),
                              fieldWithPath("project.completedAt").type(JsonFieldType.STRING)
                                  .description("프로젝트 완료일").optional(),
                              new EnumFields(ProjectStatus.class).withPath("project.projectStatus")
                                  .description("프로젝트 상태")
                          )
                      )
                      .build()
              )
          ));
    }

    @Test
    @DisplayName("프로젝트가 존재하지 않은 경우 실패")
    void failNotExistProject() throws Exception {
      // given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var projectId = 1L;
      var updateProjectRequest = new UpdateProjectRequest(null, ProjectStatus.PROJECT_COMPLETE);

      given(projectService.updateProject(user.getId(), projectId, updateProjectRequest)).willThrow(
          new BusinessException(ErrorCode.NOT_EXIST_PROJECT_EXCEPTION)
      );

      // when
      var result = mockMvc.perform(patch(URL, projectId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateProjectRequest))
          .with(user(principal))
          .with(csrf())
      );
      // then
      result.andExpect(status().isNotFound())
          .andDo(document("update-project-fail-not-exist-project",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("project")
                      .summary("프로젝트 수정 실패")
                      .description("프로젝트가 존재하지 않는 경우 실패")
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
    @DisplayName("프로젝트 멤버가 아닌 경우 실패")
    void failNotProjectMember() throws Exception {
      // given
      var user = UserBuildUtil.buildUser().build();
      var principal = getPrincipalDetails(user.getId(), user.getUserRole());
      var projectId = 1L;
      var updateProjectRequest = new UpdateProjectRequest(null, ProjectStatus.PROJECT_COMPLETE);

      given(projectService.updateProject(user.getId(), projectId, updateProjectRequest)).willThrow(
          new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION)
      );
      // when
      var result = mockMvc.perform(patch(URL, projectId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateProjectRequest))
          .with(user(principal))
          .with(csrf())
      );
      // then
      result.andExpect(status().isNotFound())
          .andDo(document("update-project-fail-not-project-member",
              resource(
                  ResourceSnippetParameters.builder()
                      .tag("project")
                      .summary("프로젝트 수정 실패")
                      .description("프로젝트 멤버가 아닌 경우 실패")
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
}