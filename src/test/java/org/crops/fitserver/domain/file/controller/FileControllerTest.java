package org.crops.fitserver.domain.file.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.crops.fitserver.domain.file.constant.FileDomain;
import org.crops.fitserver.domain.file.controller.FileController;
import org.crops.fitserver.domain.file.dto.PreSignedUrlDto;
import org.crops.fitserver.domain.file.dto.request.GeneratePreSignedUrlRequest;
import org.crops.fitserver.domain.file.service.FileService;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(FileController.class)
class FileControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private FileService fileService;

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
  void generatePreSignedUrl_failed_when_parameter_is_null() throws Exception {
    //given
    var url = "/v1/file/pre-signed-url";
    var uploadImageRequest = new GeneratePreSignedUrlRequest(null, FileDomain.CHAT);

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(uploadImageRequest))
    );

    //then
    result.andExpect(status().isBadRequest())
        .andDo(
            document("generate-pre-signed-url-failed",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("file")
                        .description("generate pre-signed url")
                        .summary("generate pre-signed url")
                        .requestSchema(Schema.schema("generatePreSignedUrlRequest"))
                        .requestFields(
                            fieldWithPath("fileName").description("파일 이름"),
                            new EnumFields(FileDomain.class).withPath("fileDomain")
                                .description("파일 도메인")
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
  void generatePreSignedUrl_Success() throws Exception {
    //given
    var url = "/v1/file/pre-signed-url";
    var fileKey = "test.jpg";
    var uploadImageRequest = new GeneratePreSignedUrlRequest(fileKey, FileDomain.PROFILE_IMAGE);
    given(fileService.generatePreSignedUrl(anyString(), any(FileDomain.class))).willReturn(
        PreSignedUrlDto.builder()
            .preSignedUrl("https://test.com")
            .fileKey(fileKey)
            .build());

    //when
    var result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(uploadImageRequest))
    );

    //then
    result.andExpect(status().isOk())
        .andDo(
            document("generate-pre-signed-url",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("file")
                        .description("generate pre-signed url")
                        .summary("generate pre-signed url")
                        .requestSchema(Schema.schema("generatePreSignedUrlRequest"))
                        .requestFields(
                            fieldWithPath("fileName").type(JsonFieldType.STRING)
                                .description("파일 이름"),
                            new EnumFields(FileDomain.class).withPath("fileDomain")
                                .description("파일 도메인")
                        )
                        .responseSchema(Schema.schema("preSignedUrlDto"))
                        .responseFields(
                            fieldWithPath("preSignedUrl").type(JsonFieldType.STRING)
                                .description("pre-signed url"),
                            fieldWithPath("fileKey").type(JsonFieldType.STRING)
                                .description("파일 키")
                        )
                        .build()
                )
            )
        );
  }

}