package org.crops.fitserver.domain.school.controller;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.crops.fitserver.domain.school.constant.SchoolType;
import org.crops.fitserver.domain.school.dto.SchoolDto;
import org.crops.fitserver.domain.school.service.SchoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(SchoolController.class)
public class SchoolControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private SchoolService schoolService;

  private final ObjectMapper objectMapper = new ObjectMapper();


  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withResponseDefaults(Preprocessors.prettyPrint())
        )
        .build();
  }


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
                            fieldWithPath("[]").type(JsonFieldType.ARRAY).description("학교 리스트"),
                            fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("학교 이름"),
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("학교 id"),
                            new EnumFields(SchoolType.class).withPath("[].type")
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
                            fieldWithPath("[]").type(JsonFieldType.ARRAY).description("학교 리스트"),
                            fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("학교 이름"),
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("학교 id"),
                            new EnumFields(SchoolType.class).withPath("[].type")
                                .description("학교 타입")
                        )
                        .build()
                )
            )
        );
  }

}
