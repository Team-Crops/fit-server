package org.crops.fitserver.domain.region.controller;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.crops.fitserver.config.MockMvcDocs;
import org.crops.fitserver.domain.region.dto.RegionDto;
import org.crops.fitserver.domain.region.dto.request.CreateRegionRequest;
import org.crops.fitserver.domain.region.dto.request.UpdateRegionRequest;
import org.crops.fitserver.domain.region.service.RegionService;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(RegionController.class)
class RegionControllerTest extends MockMvcDocs {

  @MockBean
  RegionService regionService;

  @Test
  public void getRegionList_Success() throws Exception {
    //given
    var url = "/v1/region";
    given(regionService.getRegionList()).willReturn(List.of(
        RegionDto.of(1L, "test")
    ));

    //when
    var result = mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON)
    );
    //then
    result.andExpect(status().isOk())
        .andDo(
            document("get-region-list",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("region")
                        .description("지역 리스트 조회")
                        .summary("지역 리스트 조회")
                        .responseFields(
                            fieldWithPath("regionList[]").type(JsonFieldType.ARRAY).description("지역 리스트"),
                            fieldWithPath("regionList[].id").type(JsonFieldType.NUMBER).description("지역 id"),
                            fieldWithPath("regionList[].displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .build()
                )
            )
        )
    ;
  }

  @Test
  public void createRegion_failed_display_message_empty() throws Exception {
    //given
    var url = "/v1/region";
    var request = objectMapper
        .writeValueAsString(
            new CreateRegionRequest("")
        );
    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(request)
    );
    //then
    result.andExpect(status().isBadRequest())
        .andDo(
            document("create-region-failed-display-message-empty",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("region")
                        .description("지역 생성 실패 - 지역명이 비어있음")
                        .summary("지역 생성 실패 - 지역명이 비어있음")
                        .requestSchema(Schema.schema("CreateRegionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        )
    ;
  }

  @Test
  public void createRegion_failed_Duplicate_display_message() throws Exception {
    //given
    var url = "/v1/region";
    var request = objectMapper
        .writeValueAsString(
            new CreateRegionRequest("test")
        );
    given(regionService.createRegion(any())).willThrow(
        new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION)
    );

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(request)
    );

    //then

    result.andExpect(status().isConflict())
        .andDo(
            document("create-region-failed-Duplicate-display-message",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("region")
                        .description("지역 생성 실패 - 지역명이 중복됨")
                        .summary("지역 생성 실패 - 지역명이 중복됨")
                        .requestSchema(Schema.schema("CreateRegionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .responseFields(
                            fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("에러 메시지")
                        )
                        .build()
                )
            )
        )
    ;
  }

  @Test
  public void createRegion_Success() throws Exception {
    //given
    var url = "/v1/region";
    var request = objectMapper
        .writeValueAsString(
            new CreateRegionRequest("test")
        );
    given(regionService.createRegion(any())).willReturn(RegionDto.of(1L, "test"));

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(request)
    );

    //then
    result.andExpect(status().isCreated())
        .andDo(
            document("create-region-success",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("region")
                        .description("지역 생성 성공")
                        .summary("지역 생성 성공")
                        .requestSchema(Schema.schema("CreateRegionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("지역 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .build()
                )
            )
        )
    ;
  }

  @Test
  public void updateRegion_Success() throws Exception {
    //given
    var url = "/v1/region/1";
    var request = objectMapper
        .writeValueAsString(
            new UpdateRegionRequest("test")
        );
    given(regionService.updateRegion(any(), any()))
        .willReturn(RegionDto.of(1L, "test"));

    //when
    var result = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(request)
    );

    //then
    result.andExpect(status().isNoContent())
        .andDo(
            document("update-region-success",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("region")
                        .description("지역 수정 성공")
                        .summary("지역 수정 성공")
                        .requestSchema(Schema.schema("UpdateRegionRequest"))
                        .requestFields(
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("지역 id"),
                            fieldWithPath("displayName").type(JsonFieldType.STRING)
                                .description("지역명")
                        )
                        .build()
                )
            )
        )
    ;
  }

  @Test
  public void deleteRegion_Success() throws Exception {
    //given
    var url = "/v1/region/1";

    //when
    var result = mockMvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON)
    );

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("delete-region-success",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("region")
                        .description("지역 삭제 성공")
                        .summary("지역 삭제 성공")
                        .build()
                )
            )
        )
    ;
  }
}
