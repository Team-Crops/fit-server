package org.crops.fitserver.domain.skillset.controller;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.service.SkillSetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(SkillSetController.class)
class SkillSetControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private SkillSetService skillSetService;

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
                            fieldWithPath("[]").description("직군 리스트"),
                            fieldWithPath("[].id").description("직군 id"),
                            fieldWithPath("[].displayName").description("직군 이름"),
                            fieldWithPath("[].skills").description("직군에 속한 스킬 리스트"),
                            fieldWithPath("[].skills[].id").description("스킬 id"),
                            fieldWithPath("[].skills[].displayName").description("스킬 이름")
                        )
                        .build()
                )
            )
        );
  }


}
