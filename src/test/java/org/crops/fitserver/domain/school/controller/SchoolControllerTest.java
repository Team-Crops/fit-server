package org.crops.fitserver.domain.school.controller;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.List;
import org.crops.fitserver.util.MockMvcDocs;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.dto.SchoolDto;
import org.crops.fitserver.domain.school.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(SchoolController.class)
class SchoolControllerTest extends MockMvcDocs {

  @MockBean
  SchoolService schoolService;

  @Test
  public void getSchoolList() throws Exception {
    // given
    var url = "/v1/school";
    given(schoolService.getSchoolList()).willReturn(List.of(
        SchoolDto.builder()
            .name("서울대학교")
            .id(1L)
            .type(SchoolType.UNIVERSITY)
            .build(),
        SchoolDto.builder()
            .name("서울과학기술대학교")
            .id(2L)
            .type(SchoolType.UNIVERSITY)
            .build(),
        SchoolDto.builder()
            .name("고려대학교")
            .id(3L)
            .type(SchoolType.UNIVERSITY)
            .build()
    ));

    // when
    var result = mockMvc.perform(get(url));
    // then
    result.andExpect(status().isOk())
        .andDo(
            document("get-school-list",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("school")
                        .summary("학교 리스트 조회")
                        .description("get school list")
                        .responseFields(
                            fieldWithPath("schoolList[]").type(JsonFieldType.ARRAY).description("학교 리스트"),
                            fieldWithPath("schoolList[].name").type(JsonFieldType.STRING)
                                .description("학교 이름"),
                            fieldWithPath("schoolList[].id").type(JsonFieldType.NUMBER).description("학교 id"),
                            new EnumFields(SchoolType.class).withPath("schoolList[].type")
                                .description("학교 타입")
                        )
                        .build()
                )
            )
        );
  }

  @Test
  public void getSchoolListByKeyword() throws Exception {
    // given
    var url = "/v1/school";
    given(schoolService.getSchoolListByKeyword("서울")).willReturn(List.of(
        SchoolDto.builder()
            .name("서울대학교")
            .id(1L)
            .type(SchoolType.UNIVERSITY)
            .build(),
        SchoolDto.builder()
            .name("서울과학기술대학교")
            .id(2L)
            .type(SchoolType.UNIVERSITY)
            .build()
    ));

    // when
    var result = mockMvc.perform(get(url)
        .param("keyword", "서울")
    );
    // then
    result.andExpect(status().isOk())
        .andDo(
            document("get-school-list-by-keyword",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("school")
                        .summary("학교 리스트 조회")
                        .description("get school list by keyword")
                        .responseFields(
                            fieldWithPath("schoolList[]").type(JsonFieldType.ARRAY).description("학교 리스트"),
                            fieldWithPath("schoolList[].name").type(JsonFieldType.STRING)
                                .description("학교 이름"),
                            fieldWithPath("schoolList[].id").type(JsonFieldType.NUMBER).description("학교 id"),
                            new EnumFields(SchoolType.class).withPath("schoolList[].type")
                                .description("학교 타입")
                        )
                        .build()
                )
            )
        );
  }

}
