package org.crops.fitserver.domain.skillset.controller;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.AddSkillListToPositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.AddSkillToPositionListRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;
import org.crops.fitserver.domain.skillset.dto.request.UpdatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.UpdateSkillRequest;
import org.crops.fitserver.domain.skillset.service.SkillSetService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.util.MockMvcDocs;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(SkillSetController.class)
class SkillSetControllerTest extends MockMvcDocs {

  @MockBean
  SkillSetService skillSetService;

  @Test
  public void getPositionList_success() throws Exception {

    //given
    var url = "/v1/skill-set/position";
    given(skillSetService.getPositionList()).willReturn(List.of(
            PositionDto.builder()
                .id(1L)
                .displayName("백엔드")
                .skillList(List.of(
                    SkillDto.builder()
                        .id(1L)
                        .displayName("Java")
                        .build(),
                    SkillDto.builder()
                        .id(2L)
                        .displayName("Spring")
                        .build()
                ))
                .build(),
            PositionDto.builder()
                .id(2L)
                .displayName("프론트엔드")
                .skillList(List.of(
                    SkillDto.builder()
                        .id(3L)
                        .displayName("React")
                        .build(),
                    SkillDto.builder()
                        .id(4L)
                        .displayName("Vue")
                        .build()
                ))
                .build()
        )
    );

    //when
    var result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/position/get-position-list",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 리스트 조회")
                        .description("직군 리스트를 조회한다.")
                        .responseSchema(Schema.schema("position-list"))
                        .responseFields(
                            fieldWithPath("positionList[]").type(JsonFieldType.ARRAY)
                                .description("직군 리스트"),
                            fieldWithPath("positionList[].id").type(JsonFieldType.NUMBER)
                                .description("직군 id"),
                            fieldWithPath("positionList[].displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("positionList[].skillList").type(JsonFieldType.ARRAY)
                                .description("직군에 속한 스킬 리스트"),
                            fieldWithPath("positionList[].skillList[].id").type(
                                    JsonFieldType.NUMBER)
                                .description("스킬 id"),
                            fieldWithPath("positionList[].skillList[].displayName").type(
                                    JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void create_position_failed_duplicate_displayName() throws Exception {
    //given
    var url = "/v1/skill-set/position";
    CreatePositionRequest createPositionRequest = CreatePositionRequest.builder()
        .displayName("test").imageUrl("test").build();
    given(skillSetService.createPosition(any())).willThrow(
        new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createPositionRequest))
    );

    //then
    result.andExpect(status().isConflict())
        .andDo(
            document("skill-set/position/create-position-failed",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 생성")
                        .description("직군을 생성한다.")
                        .requestSchema(Schema.schema("createPositionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                .description("직군 이미지 url(profileDefault)"),
                            fieldWithPath("skillIds").type(JsonFieldType.ARRAY)
                                .description("직군에 속한 스킬 리스트").optional()
                        )
                        .responseSchema(Schema.schema("errorResponse"))
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void create_position_success() throws Exception {
    //given
    var url = "/v1/skill-set/position";
    CreatePositionRequest createPositionRequest = CreatePositionRequest.builder()
        .displayName("test").imageUrl("test").build();
    given(skillSetService.createPosition(any())).willReturn(PositionDto.builder()
        .id(1L)
        .displayName("test")
        .build());

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createPositionRequest))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(
            document("skill-set/position/create-position",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 생성")
                        .description("직군을 생성한다.")
                        .requestSchema(Schema.schema("createPositionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                .description("직군 이미지 url(profileDefault)"),
                            fieldWithPath("skillIds").description("직군에 속한 스킬 리스트").optional()
                        )
                        .responseSchema(Schema.schema("position"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("직군 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("skillList")
                                .description("직군에 속한 스킬 리스트")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void create_position_with_skills_fail_not_found() throws Exception {
    //given
    var url = "/v1/skill-set/position";
    CreatePositionRequest createPositionRequest = CreatePositionRequest.builder()
        .displayName("test")
        .imageUrl("test")
        .skillIds(List.of(1L, 2L))
        .build();
    given(skillSetService.createPosition(any())).willThrow(
        new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createPositionRequest))
    );

    //then
    result.andExpect(status().isNotFound())
        .andDo(
            document("skill-set/position/create-position-with-skills-fail-not-found",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 생성")
                        .description("직군을 생성한다.")
                        .requestSchema(Schema.schema("createPositionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                .description("직군 이미지 url(profileDefault)"),
                            fieldWithPath("skillIds").type(JsonFieldType.ARRAY)
                                .description("직군에 속한 스킬 리스트").optional()
                        )
                        .responseSchema(Schema.schema("errorResponse"))
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void create_position_with_skills_success() throws Exception {
    //given
    var url = "/v1/skill-set/position";
    CreatePositionRequest createPositionRequest = CreatePositionRequest.builder()
        .displayName("test")
        .skillIds(List.of(1L, 2L))
        .imageUrl("test")
        .build();
    given(skillSetService.createPosition(any())).willReturn(PositionDto.builder()
        .id(1L)
        .displayName("test")
        .skillList(List.of(
            SkillDto.builder()
                .id(1L)
                .displayName("Java")
                .build(),
            SkillDto.builder()
                .id(2L)
                .displayName("Spring")
                .build()
        ))
        .build());

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createPositionRequest))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(
            document("skill-set/position/create-position-with-skills",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 생성")
                        .description("직군을 생성한다.")
                        .requestSchema(Schema.schema("createPositionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                .description("직군 이미지 url(profileDefault)"),
                            fieldWithPath("skillIds").description("직군에 속한 스킬 리스트").optional()
                        )
                        .responseSchema(Schema.schema("position"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("직군 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("skillList").type(JsonFieldType.ARRAY)
                                .description("직군에 속한 스킬 리스트"),
                            fieldWithPath("skillList[].id").type(JsonFieldType.NUMBER)
                                .description("스킬 id"),
                            fieldWithPath("skillList[].displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void update_position_displayName_fail_duplicate() throws Exception {
    //given
    var url = "/v1/skill-set/position/{positionId}";
    var updatePositionRequest = new UpdatePositionRequest(JsonNullable.of("test"),
        JsonNullable.undefined());
    given(skillSetService.updatePositionDisplayName(any(), any())).willThrow(
        new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(patch(url, 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatePositionRequest))
    );

    //then
    result.andExpect(status().isConflict())
        .andDo(
            document("skill-set/position/update-position-displayName-fail-duplicate",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 이름 수정")
                        .description("직군 이름을 수정한다.")
                        .requestSchema(Schema.schema("updatePositionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름")
                        )
                        .responseSchema(Schema.schema("errorResponse"))
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void update_position_displayName_success() throws Exception {
    //given
    var url = "/v1/skill-set/position/{positionId}";
    var updatePositionRequest = new UpdatePositionRequest(JsonNullable.of("test"),
        JsonNullable.undefined());
    given(skillSetService.updatePositionDisplayName(any(), any())).willReturn(PositionDto.builder()
        .id(1L)
        .displayName("test")
        .build());

    //when
    var result = mockMvc.perform(patch(url, 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatePositionRequest))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/position/update-position-displayName-success",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 이름 수정")
                        .description("직군 이름을 수정한다.")
                        .requestSchema(Schema.schema("updatePositionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름")
                        )
                        .responseSchema(Schema.schema("position"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("직군 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("skillList").description("직군에 속한 스킬 리스트")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void add_position_skillList_success() throws Exception {
    //given
    var url = "/v1/skill-set/position/1/skill";
    var addSkillListToPositionRequest = AddSkillListToPositionRequest.builder()
        .skillIds(List.of(1L, 2L)).build();
    given(skillSetService.addSkillListToPosition(any(), any())).willReturn(PositionDto.builder()
        .id(1L)
        .displayName("test")
        .skillList(List.of(
            SkillDto.builder()
                .id(1L)
                .displayName("Java")
                .build(),
            SkillDto.builder()
                .id(2L)
                .displayName("Spring")
                .build()
        ))
        .build());

    //when
    var result = mockMvc.perform(patch(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(addSkillListToPositionRequest))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/position/add-position-skillList",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군에 스킬 추가")
                        .description("직군에 스킬을 추가한다.")
                        .requestSchema(Schema.schema("updatePositionRequest"))
                        .requestFields(
                            fieldWithPath("skillIds").description("직군에 추가할 스킬 리스트")
                        )
                        .responseSchema(Schema.schema("position"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("직군 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("직군 이름"),
                            fieldWithPath("skillList").type(JsonFieldType.ARRAY)
                                .description("직군에 속한 스킬 리스트"),
                            fieldWithPath("skillList[].id").type(JsonFieldType.NUMBER)
                                .description("스킬 id"),
                            fieldWithPath("skillList[].displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void delete_position_success() throws Exception {
    //given
    var url = "/v1/skill-set/position/{positionId}";

    //when
    var result = mockMvc.perform(delete(url, 1L));

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/position/delete-position",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군 삭제")
                        .description("직군을 삭제한다.")
                        .build()
                )
            )
        );
  }

  @Test
  public void getSkillList_success() throws Exception {
    //given
    var url = "/v1/skill-set/skill";
    given(skillSetService.getSkillList()).willReturn(List.of(
        SkillDto.builder()
            .id(1L)
            .displayName("Java")
            .build(),
        SkillDto.builder()
            .id(2L)
            .displayName("Spring")
            .build()
    ));

    //when
    var result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/skill/get-skill-list",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 리스트 조회")
                        .description("스킬 리스트를 조회한다.")
                        .responseSchema(Schema.schema("skill-list"))
                        .responseFields(
                            fieldWithPath("skillList[]").type(JsonFieldType.ARRAY)
                                .description("스킬 리스트"),
                            fieldWithPath("skillList[].id").type(JsonFieldType.NUMBER)
                                .description("스킬 id"),
                            fieldWithPath("skillList[].displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void getSkillListByPositionId_success() throws Exception {
    //given
    var url = "/v1/skill-set/position/1/skill";
    given(skillSetService.getSkillListByPositionId(any())).willReturn(List.of(
        SkillDto.builder()
            .id(1L)
            .displayName("Java")
            .build(),
        SkillDto.builder()
            .id(2L)
            .displayName("Spring")
            .build()
    ));

    //when
    var result = mockMvc.perform(get(url));

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/skill/get-skill-list-by-position-id",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("직군에 속한 스킬 리스트 조회")
                        .description("직군에 속한 스킬 리스트를 조회한다.")
                        .responseSchema(Schema.schema("skill-list"))
                        .responseFields(
                            fieldWithPath("skillList[]").type(JsonFieldType.ARRAY)
                                .description("스킬 리스트"),
                            fieldWithPath("skillList[].id").type(JsonFieldType.NUMBER)
                                .description("스킬 id"),
                            fieldWithPath("skillList[].displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void createSkill_failed_duplicate_displayName() throws Exception {
    //given
    var url = "/v1/skill-set/skill";
    var createSkillRequest = CreateSkillRequest.builder().displayName("test").build();
    given(skillSetService.createSkill(any())).willThrow(
        new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createSkillRequest))
    );

    //then
    result.andExpect(status().isConflict())
        .andDo(
            document("skill-set/skill/create-skill-fail-duplicate",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 생성")
                        .description("스킬을 생성한다.")
                        .requestSchema(Schema.schema("createSkillRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름"),
                            fieldWithPath("positionIds")
                                .description("스킬에 속한 직군 리스트")
                        )
                        .responseSchema(Schema.schema("errorResponse"))
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void createSkill_success() throws Exception {
    //given
    var url = "/v1/skill-set/skill";
    var createSkillRequest = CreateSkillRequest.builder().displayName("test").build();
    given(skillSetService.createSkill(any())).willReturn(SkillDto.builder()
        .id(1L)
        .displayName("test")
        .build());

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createSkillRequest))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(
            document("skill-set/skill/create-skill",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 생성")
                        .description("스킬을 생성한다.")
                        .requestSchema(Schema.schema("createSkillRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름"),
                            fieldWithPath("positionIds").description("스킬에 속한 직군 리스트")
                        )
                        .responseSchema(Schema.schema("skill"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("스킬 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void createSKill_with_positionIds_fail_not_found() throws Exception {
    //given
    var url = "/v1/skill-set/skill";
    var createSkillRequest = CreateSkillRequest.builder()
        .displayName("test")
        .positionIds(List.of(1L, 2L))
        .build();
    given(skillSetService.createSkill(any())).willThrow(
        new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createSkillRequest))
    );

    //then
    result.andExpect(status().isNotFound())
        .andDo(
            document("skill-set/skill/create-skill-with-positionIds-fail-not-found",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 생성")
                        .description("스킬을 생성한다.")
                        .requestSchema(Schema.schema("createSkillRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름"),
                            fieldWithPath("positionIds").type(JsonFieldType.ARRAY)
                                .description("스킬에 속한 직군 리스트")
                        )
                        .responseSchema(Schema.schema("errorResponse"))
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void createSkill_with_positionIds_success() throws Exception {
    //given
    var url = "/v1/skill-set/skill";
    var createSkillRequest = CreateSkillRequest.builder()
        .displayName("test")
        .positionIds(List.of(1L, 2L))
        .build();
    given(skillSetService.createSkill(any())).willReturn(SkillDto.builder()
        .id(1L)
        .displayName("test")
        .build());

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createSkillRequest))
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(
            document("skill-set/skill/create-skill-with-positionIds",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 생성")
                        .description("스킬을 생성한다.(결과에서 직군 리스트는 제외된다. 201 code를 받았다면 성공한 것임)")
                        .requestSchema(Schema.schema("createSkillRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름"),
                            fieldWithPath("positionIds").type(JsonFieldType.ARRAY)
                                .description("스킬에 속한 직군 리스트")
                        )
                        .responseSchema(Schema.schema("skill"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("스킬 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void updateSkill_displayName_fail_duplicate() throws Exception {
    //given
    var url = "/v1/skill-set/skill/{skillId}";
    var updateSkillRequest = new UpdateSkillRequest(JsonNullable.of("test"));
    given(skillSetService.updateSkillDisplayName(any(), any())).willThrow(
        new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(patch(url, 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateSkillRequest))
    );

    //then
    result.andExpect(status().isConflict())
        .andDo(
            document("skill-set/skill/update-skill-displayName-fail-duplicate",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 이름 수정")
                        .description("스킬 이름을 수정한다.")
                        .requestSchema(Schema.schema("updateSkillRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .responseSchema(Schema.schema("errorResponse"))
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void updateSkill_displayName_success() throws Exception {
    //given
    var url = "/v1/skill-set/skill/{skillId}";
    var updateSkillRequest = new UpdateSkillRequest(JsonNullable.of("test"));
    given(skillSetService.updateSkillDisplayName(any(), any())).willReturn(SkillDto.builder()
        .id(1L)
        .displayName("test")
        .build());

    //when
    var result = mockMvc.perform(patch(url, 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateSkillRequest))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/skill/update-skill-displayName-success",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 이름 수정")
                        .description("스킬 이름을 수정한다.")
                        .requestSchema(Schema.schema("updateSkillRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .responseSchema(Schema.schema("skill"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("스킬 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void addSkillToPositionList_success() throws Exception {
    //given
    var url = "/v1/skill-set/skill/1/position";
    AddSkillToPositionListRequest addSkillToPositionListRequest = AddSkillToPositionListRequest.builder()
        .positionIds(List.of(1L, 2L))
        .build();
    given(skillSetService.addSkillToPositionList(any(), any())).willReturn(SkillDto.builder()
        .id(1L)
        .displayName("test")
        .build());

    //when
    var result = mockMvc.perform(patch(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(addSkillToPositionListRequest))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/skill/add-skill-to-position-list",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬에 직군 추가")
                        .description("스킬에 직군을 추가한다.")
                        .requestSchema(Schema.schema("addSkillToPositionListRequest"))
                        .requestFields(
                            fieldWithPath("positionIds").type(JsonFieldType.ARRAY)
                                .description("스킬에 추가할 직군 리스트")
                        )
                        .responseSchema(Schema.schema("skill"))
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("스킬 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void deleteSkill_success() throws Exception {
    //given
    var url = "/v1/skill-set/skill/{skillId}";

    //when
    var result = mockMvc.perform(delete(url, 1L));

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("skill-set/skill/delete-skill",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("skill-set")
                        .summary("스킬 삭제")
                        .description("스킬을 삭제한다.")
                        .build()
                )
            )
        );
  }
}
